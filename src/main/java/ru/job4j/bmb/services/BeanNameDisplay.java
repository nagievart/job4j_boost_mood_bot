package ru.job4j.bmb.services;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

@Component
public class BeanNameDisplay implements BeanNameAware {

    private String beanName;

    public void displayBeanName() {
        System.out.println("Текущий бин имеет имя:" + beanName);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
