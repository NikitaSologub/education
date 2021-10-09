package by.itacademy.sologub.controllers;

import javax.servlet.annotation.WebServlet;

import static by.itacademy.sologub.constants.Constant.FRONT_CONTROLLER;

@WebServlet(FRONT_CONTROLLER)
public class FrontController extends BaseController{

    // сюда приходит запрос со странички Login_page.jsp (минуя Filter)


}
