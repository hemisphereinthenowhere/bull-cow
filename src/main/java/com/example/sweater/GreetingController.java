package com.example.sweater;


import java.util.List;
import java.util.Map;

import com.example.sweater.domain.Message;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

	@Autowired
	private MessageRepo messageRepo;

    @GetMapping("/")
    public String greeting(Map <String, Object> model) {

        		return "greeting";
    }
    @GetMapping("/main")
    public String main(Map<String, Object> model) {
		Iterable<Message> messages = messageRepo.findAll();

		model.put("messages", messages);
    	return "main";
    }

    @PostMapping("/main")
	// @RequestParam дергает значения переменной, переданной аннотации, из формы в запросе, в данном случае постом
	public String add(@RequestParam String text, @RequestParam String tag, Map <String, Object> model) {
    	Message message = new Message(text, tag);

    	// сохраняем сообщение в репозиторий
    	messageRepo.save(message);

		// берем из репозитория сообщения
		Iterable<Message> messages = messageRepo.findAll();

		// кладем сообщения в модель
		model.put("messages", messages);

    	return "main";
	}

	@PostMapping("filter")
	public String filter(@RequestParam String filter, Map <String, Object> model) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.put("messages", messages);
    	return "main";
	}
}