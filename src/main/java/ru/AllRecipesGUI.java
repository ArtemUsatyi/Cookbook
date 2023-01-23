package ru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AllRecipesGUI implements ActionListener {
    public JFrame frame;
    private JPanel panel = new JPanel();
    private JPanel panelTop = new JPanel();
    private JPanel panelRight = new JPanel();
    private JButton buttonAddRecipe = new JButton("Добавить рецепт");
    private JButton buttonSearch = new JButton("Найти");
    private String[] categories = {"Все", "Супы", "Мясо", "Рыба", "Салаты", "Гарниры", "Напитки"};
    private JComboBox comboBox = new JComboBox(categories);
    private JLabel labelCategory = new JLabel("Сортировать по категориям :");
    private JScrollPane scroll = new JScrollPane();
    private JButton[] buttonRecipes = new JButton[10];
    private ControlBDRecipes bd = new ControlBDRecipes();
    private List<String> nameRecipe;

    public AllRecipesGUI() {
        insertNameRecipesCategory((String) comboBox.getSelectedItem());
    }

    public void lookRecipes() {
        frame = new JFrame("Кулинарная книга");
        buttonAddRecipe.setFocusPainted(false);
        buttonSearch.setFocusPainted(false);

        panelTop.add(labelCategory);
        panelTop.add(comboBox);
        panelTop.add(buttonSearch);
        panelTop.setLayout(new FlowLayout(FlowLayout.LEFT));

        scroll.add(panel);
        scroll = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panelRight.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelRight.setPreferredSize(new Dimension(150, 300));
        panelRight.add(buttonAddRecipe);

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteNameRecipe();
                insertNameRecipesCategory((String) comboBox.getSelectedItem());
                frame.invalidate();
                frame.validate();
                frame.repaint();
            }
        });

        buttonAddRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AddNewRecipeGUI newRecipe = new AddNewRecipeGUI();
                newRecipe.createNewRecipe();
            }
        });

        frame.getContentPane().add(BorderLayout.NORTH, panelTop);
        frame.getContentPane().add(BorderLayout.CENTER, scroll);
        frame.getContentPane().add(BorderLayout.EAST, panelRight);
        frame.setSize(600, 500);                    // Размер окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Закрытие формы "на крестик"
        frame.setResizable(false);                              // Редактировать форму запрещено
        frame.setLocationRelativeTo(null);                      // Позиция окна в центре экрана
        frame.setVisible(true);                                 // Отображение формы
    }

    private void insertNameRecipesCategory(String nameCategory) {
        nameRecipe = bd.selectNamesRecipe(nameCategory);
        int size = nameRecipe.size();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        for (int i = 0; i < size; i++) {
            buttonRecipes[i] = new JButton(nameRecipe.get(i));
            buttonRecipes[i].addActionListener(this);                          // добавить кнопку ActionListener для отслеживания нажатия
            buttonRecipes[i].setFocusPainted(false);                              // убрать рамку вокруг текста
            buttonRecipes[i].setContentAreaFilled(false);                         // убрать цвет и заливку кнопки в нажатом состоянии
            panel.add(buttonRecipes[i]);
        }
    }

    private void deleteNameRecipe() {
        panel.removeAll();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.dispose();
        OneRecipeGUI oneRecipeGUI = new OneRecipeGUI(e.getActionCommand());
    }
}
