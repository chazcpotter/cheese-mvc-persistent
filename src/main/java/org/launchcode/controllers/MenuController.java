package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by kajuh_000 on 6/22/2017.
 */

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model model){

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");
        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAdd(Model model){

        model.addAttribute("menu", new Menu());
        model.addAttribute("title", "Add Menu");
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAdd(@ModelAttribute @Valid Menu newMenu, Errors errors, Model model){

        if(errors.hasErrors()){

            return "menu/add";

        } else{

            menuDao.save(newMenu);
            return "redirect:view/" + newMenu.getId();

        }
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String displayMenu(@PathVariable int menuId, Model model){

        model.addAttribute("menu", menuDao.findOne(menuId));
        model.addAttribute("title", menuDao.findOne(menuId).getName());
        return "menu/view";
    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(@PathVariable int menuId, Model model){

        Menu menu = menuDao.findOne(menuId);
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        AddMenuItemForm newMenu = new AddMenuItemForm(menu, cheeses);

        model.addAttribute("form", newMenu);
        model.addAttribute("title", "Add Item to Menu: " + menu.getName());

        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid AddMenuItemForm form, Errors errors, Model model){

        if(errors.hasErrors()){
            return "menu/add-item";
        } else {
            Menu theMenu = menuDao.findOne(form.getMenuId());
            Cheese theCheese = cheeseDao.findOne(form.getCheeseId());

            theMenu.addItem(theCheese);

            menuDao.save(theMenu);

            return "redirect:view/" + theMenu.getId();
        }
    }
}
