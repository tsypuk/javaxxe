package ua.in.smartjava;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class SimpsonController {

    @Autowired
	SimpsonService simpsonService;
	
	@RequestMapping(method=RequestMethod.GET)
	public String hello(Locale locale, Model model) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);        
		String formattedDate = dateFormat.format(date);
		model.addAttribute("currentTime", formattedDate);
        model.addAttribute("java", System.getProperty("java.version"));
		return "hello";
	}

	@RequestMapping("/about")
	public String about(Model model) {
	    model.addAttribute("author", "Tsypuk Roman");
	    return "about";
    }

	@RequestMapping("/table")
	public String table(Model model) {
	    model.addAttribute("simpsons", simpsonService.getSimpsons());
		model.addAttribute("tasks", Arrays.asList("Lohika", "Test", "TTT"));
		return "table";
	}

	@RequestMapping("/upload")
	public String upload(Model model) {
//		model.addAttribute("simpsons", simpsonService.getSimpsons());
//		model.addAttribute("tasks", Arrays.asList("Lohika", "Test", "TTT"));
		return "upload";
	}



}