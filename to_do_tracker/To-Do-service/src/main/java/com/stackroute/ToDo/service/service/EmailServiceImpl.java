package com.stackroute.ToDo.service.service;


import com.stackroute.ToDo.service.exception.UserNotFoundException;
import com.stackroute.ToDo.service.model.Task;
import com.stackroute.ToDo.service.model.User;
import com.stackroute.ToDo.service.rabbitMQ.EmailDTO;
import com.stackroute.ToDo.service.rabbitMQ.MailProducer;
import com.stackroute.ToDo.service.repository.UserToDoServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private MailProducer mailProducer;
//
//    @Autowired
//    private JavaMailSender javaMailSender;

    @Autowired
    private UserToDoServiceRepository repository;

    @Value("${spring.mail.username}") private String sender;
    @Override
    public String sendSimpleMail(User details)
    {
        try {
            EmailDTO emailDTO = new EmailDTO(details.getUserEmailId(), "Welcome to To-Do Tracker Application", "Signup is success");
            mailProducer.sendEmailDtoQueue(emailDTO);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void feedBack(String emailId) {
        EmailDTO emailDTO = new EmailDTO(emailId, "Your Feedback Is Valuable For Us,We Will Consider Your Valuable Feed Back in Further Update", "Feedback");
        mailProducer.sendEmailDtoQueue(emailDTO);
    }

    @Override
    public void feedBackToAdmin(String message, String from) {
        EmailDTO emailDTO = new EmailDTO("dummycapstonproject@gmail.com", message, "Received feedback from "+from);
        mailProducer.sendEmailDtoQueue(emailDTO);
    }

    @Override
    public void updateProfile(User user) {
        EmailDTO emailDTO = new EmailDTO(user.getUserEmailId(), "Your Profile Has been Updated", "Profile Updated");
        mailProducer.sendEmailDtoQueue(emailDTO);

    }

    Comparator<Task> dueDateComparator = ((task1, task2) -> {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        LocalDate dueDate1 = null;
        try {
            dueDate1 = new SimpleDateFormat("dd/MM/yyyy").parse(task1.getTaskEndDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        LocalDate dueDate2 = null;
        try {
            dueDate2 = new SimpleDateFormat("dd/MM/yyyy").parse(task2.getTaskEndDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (dueDate1.isBefore(dueDate2)) {
            return -1;
        }
        if (dueDate1.isAfter(dueDate2)) {
            return 1;
        }
        return 0;
    });

    @Override
    public List<Task> getTasksWithNearDueDate(String userEmail) throws UserNotFoundException, ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Optional<User> user = repository.findById(userEmail);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<Task> allTasks = user.get().getUserTask();
        List<Task> tasksNearingDueDate = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.isStatus()) {
                continue;
            }
            LocalDate dueDate =new SimpleDateFormat("dd/MM/yyyy").parse(task.getTaskEndDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate tomorrowDate = LocalDate.now().plusDays(1);
            if (dueDate.isAfter(tomorrowDate) || dueDate.isBefore(LocalDate.now())){
                continue;
            } else {
                tasksNearingDueDate.add(task);
            }
        }
        return tasksNearingDueDate.stream().sorted(dueDateComparator).collect(Collectors.toList());
    }

    @Override
    public List<Task> getTasksWithOverDue(String userEmail) throws UserNotFoundException,ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Optional<User> user = repository.findById(userEmail);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<Task> allTasks = user.get().getUserTask();
        List<Task> tasksOverDue = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.isStatus()) {
                continue;
            }
            LocalDate dueDate =new SimpleDateFormat("dd/MM/yyyy").parse(task.getTaskEndDate()).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (dueDate.isBefore(LocalDate.now())) {
                tasksOverDue.add(task);
            }
        }
        return tasksOverDue.stream().sorted(dueDateComparator).collect(Collectors.toList());
    }

    @Override
    public String emailReminders() throws UserNotFoundException, ParseException
//            , MessagingException
    {
//        List<User> users = repository.findAll();
//        StringBuilder outputMessage = new StringBuilder("\n");
//
//        for (User user: users) {
//            List<Task> overDueTasks = getTasksWithOverDue(user.getUserEmailId());
//            List<Task> nearDueTasks = getTasksWithNearDueDate(user.getUserEmailId());
//
//            if (overDueTasks.isEmpty() && nearDueTasks.isEmpty()) {
//                outputMessage.append("\nThe user [userID: " + user.getUserEmailId() + "] currently has no overdue/neardue tasks to notify!");
//                continue;
//            }
//
//           MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
//
//            messageHelper.setTo(user.getUserEmailId());
//            messageHelper.setSubject("From To-Do Reminder");
//
//            StringBuilder mailBody = new StringBuilder("    "+"<h2>Dear " + user.getUserName()+ "! This email is a reminder from 'ToDo Tracker' regarding your important pending tasks....</h2>");
//
//            if (!overDueTasks.isEmpty()) {
//                mailBody.append("<br><h3 style=\"color: red;\">Over Due Tasks</h3><ol>");
//                overDueTasks.forEach((t) -> mailBody.append("<li style=\"color: red;\"> Task Heading : '" + t.getTaskHeading() + "'   <br>   Due date : " + t.getTaskEndDate() + "   <br>   Priority Level : " + t.getPriority() + "</li>"));
//                mailBody.append("</ol>");
//            }
//
//            if (!nearDueTasks.isEmpty()) {
//
//                mailBody.append("<br><h3 style=\"color: #4945d6;\">Nearing Due Tasks</h3><ol>");
//                nearDueTasks.forEach((t) -> mailBody.append("<li> Task Heading : '" + t.getTaskHeading() + "'  <br>   Due date : " + t.getTaskEndDate() + "   <br>   Priority Level : " + t.getPriority() + "</li>"));
//                mailBody.append("</ol>");
//            }
//
//            mailBody.append("<br><p>To view more details of the above notified tasks or to update them, please login to your 'ToDo Tracker' application..</p><br><h4>with Regards, </h4><br><h4>from ToDo Tracker Team</h4>");
//
//            messageHelper.setText(mailBody.toString(), true);
//
//            javaMailSender.send(mimeMessage);
//            outputMessage.append("\nThe user EmailId: " + user.getUserEmailId() + " has been successfully notified of their tasks via email!");
//
//        }
//        return outputMessage.toString() + "\n";
        return null;
    }
}
