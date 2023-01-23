package ru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OneRecipeGUI {
    private JFrame frame;
    private JLabel labelNameRecipe = new JLabel();
    private JLabel labelIngredient = new JLabel("Список ингредиентов :");
    private JLabel labelMargin = new JLabel("**************************************************************************************");
    private JLabel labelMargin1 = new JLabel("**************************************************************************************");
    private JLabel labelMargin2 = new JLabel("**************************************************************************************");
    private JLabel labelDescription = new JLabel("Описание рецепта.");
    private JTextArea area = new JTextArea(10, 39);
    private JLabel[] labelListIngredients = new JLabel[18];
    private JButton buttonEdit = new JButton("Редактировать");
    private JButton buttonBack = new JButton("Выйти в меню");
    private JScrollPane scroller;
    private JPanel panel = new JPanel();
    private JPanel panelPut = new JPanel();
    private JPanel panelRight = new JPanel();
    private String nameRecipe;
    private ControlBDRecipes bd = new ControlBDRecipes();

    public OneRecipeGUI(String nameRecipe) {
        this.nameRecipe = nameRecipe;
        viewRecipe();
    }

    public void viewRecipe() {
        frame = new JFrame("Кулинарная книга");
        buttonEdit.setFocusPainted(false);
        buttonBack.setFocusPainted(false);
        labelNameRecipe.setText("Название рецепта : " + nameRecipe);
        panel.add(labelNameRecipe);
        panel.add(labelMargin);
        panel.add(labelDescription);
        descriptionRecipe();
        panel.add(labelIngredient);
        panel.add(labelMargin1);

        listIngredients();
        panel.add(panelPut);

        panelRight.add(buttonEdit);
        panelRight.add(buttonBack);
        panelRight.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelRight.setPreferredSize(new Dimension(150, 300));

        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AllRecipesGUI all = new AllRecipesGUI();
                all.lookRecipes();
            }
        });

        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                EditRecipeGUI edit = new EditRecipeGUI(nameRecipe);
            }
        });

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.EAST, panelRight);

        frame.setSize(600, 800);                    // Размер окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Закрытие формы "на крестик"
        frame.setResizable(false);                              // Редактировать форму запрещено
        frame.setLocationRelativeTo(null);                      // Позиция окна в центре экрана
        frame.setVisible(true);                                 // Отображение формы
    }

    private void descriptionRecipe(){
        scroller = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        area.append(bd.selectDescription(nameRecipe));
        panel.add(scroller);
    }

    private void listIngredients() {
        panelPut.setLayout(new BoxLayout(panelPut, BoxLayout.Y_AXIS));
        List<String> list = bd.selectRecipe(nameRecipe);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            labelListIngredients[i] = new JLabel();
            labelListIngredients[i].setText(list.get(i));
            panelPut.add(labelListIngredients[i]);
        }
        panelPut.add(labelMargin2);
    }
}
