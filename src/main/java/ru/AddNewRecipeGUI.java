package ru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddNewRecipeGUI {
    public JFrame frame;
    private JPanel panel = new JPanel();
    private JPanel panelRight = new JPanel();
    private JTextField textFieldNameRecipe = new JTextField(32);
    private JTextField[] textFieldIngredients = new JTextField[18];
    private JTextField[] textFieldValueIng = new JTextField[18];
    private JButton buttonAddRecipe = new JButton("Добавить ингридиент");
    private JButton buttonDelete = new JButton("Удалить ингридиент");
    private JButton buttonSave = new JButton("Сохранить рецепт");
    private JButton buttonBack = new JButton("Выйти в меню");
    private JLabel labelNameRecipe = new JLabel("Название блюда: ");
    private JLabel labelDescriptionRecipe = new JLabel("Описание приготовления:");
    private JLabel labelNameIngredient = new JLabel("Название продуктов:                                                              ");
    private JLabel labelAmountIngredient = new JLabel("Кол-во:  ");
    private JLabel labelGram = new JLabel("Мл/Г:        ");
    private JTextArea area = new JTextArea(10, 42);
    private JLabel labelCategory = new JLabel("Категория рецепта :");
    private String[] itemsCategory = {"", "Супы", "Мясо", "Рыба", "Салаты", "Гарниры", "Напитки"};
    private JComboBox comboBoxCategory = new JComboBox(itemsCategory);
    private int numberText = 0;
    private String[] items = {"г", "шт", "мл", "кг", "банка", "упак"};
    private JComboBox[] comboBox = new JComboBox[18];
    private JScrollPane scroller;
    private ControlBDRecipes bd = new ControlBDRecipes();
    private String nameRecipe;

    public void createNewRecipe() {
        frame = new JFrame("Добавить новый рецепт");
        buttonAddRecipe.setFocusPainted(false);
        buttonSave.setFocusPainted(false);
        buttonDelete.setFocusPainted(false);
        buttonBack.setFocusPainted(false);

        buttonAddRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewIngredients();
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteIngredients();
            }
        });
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                AllRecipesGUI view = new AllRecipesGUI();
                view.lookRecipes();
            }
        });
        scroller = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(labelNameRecipe);
        panel.add(textFieldNameRecipe);
        panel.add(labelDescriptionRecipe);
        panel.add(scroller);
        panel.add(labelNameIngredient);
        panel.add(labelAmountIngredient);
        panel.add(labelGram);
        textFieldIngredients[0] = new JTextField(32);
        textFieldValueIng[0] = new JTextField(5);
        comboBox[0] = new JComboBox<>(items);
        panel.add(textFieldIngredients[0]);
        panel.add(textFieldValueIng[0]);
        panel.add(comboBox[0]);

        panelRight.add(buttonAddRecipe);
        panelRight.add(buttonDelete);
        panelRight.add(buttonSave);
        panelRight.add(labelCategory);
        panelRight.add(comboBoxCategory);
        panelRight.add(buttonBack);
        panelRight.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelRight.setPreferredSize(new Dimension(170, 300));

        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = (String) comboBoxCategory.getSelectedItem();
                if (category.equals("")) {
                    JOptionPane.showMessageDialog(frame,
                            "Категория рецепта не должна быть пустым. Выберите категорию для рецепта.",
                            "Сохранение рецепта в базе данных.",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (textFieldNameRecipe.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Название рецепта не должна быть пустым. Введите название рецепта.",
                            "Сохранение рецепта в базе данных.",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String emptyIng = ingredientsIsEmpty();
                if (emptyIng.equals("null")) return;

                if(area.getText().isEmpty()){
                    JOptionPane.showMessageDialog(frame,
                            "Описание рецепта пустое. Заполните описание.",
                            "Сохранение рецепта в базе данных",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (bd.validatorNameRecipe(textFieldNameRecipe.getText())){
                    JOptionPane.showMessageDialog(frame,
                            "Такое название рецепта существует. Измените название.",
                            "Сохранение рецепта в базе данных.",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                bd.insertNameRecipe(textFieldNameRecipe.getText(), (String) comboBoxCategory.getSelectedItem());
                nameRecipe = textFieldNameRecipe.getText();
                textFieldNameRecipe.setText("");
                saveIngredients();
                bd.insertDescription(nameRecipe,area.getText());
                area.setText("");
            }
        });

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.EAST, panelRight);
        frame.setSize(650, 800);                    // Размер окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Закрытие формы "на крестик"
        frame.setResizable(false);                              // Редактировать форму запрещено
        frame.setLocationRelativeTo(null);                      // Позиция окна в центре экрана
        frame.setVisible(true);                                 // Отображение формы
    }

    private void addNewIngredients() {
        if (numberText == 17) return;
        numberText++;
        textFieldIngredients[numberText] = new JTextField(32);
        textFieldValueIng[numberText] = new JTextField(5);
        comboBox[numberText] = new JComboBox<>(items);
        panel.add(textFieldIngredients[numberText]);
        panel.add(textFieldValueIng[numberText]);
        panel.add(comboBox[numberText]);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    private void deleteIngredients() {
        if (numberText == 0) return;
        panel.remove(textFieldIngredients[numberText]);
        panel.remove(textFieldValueIng[numberText]);
        panel.remove(comboBox[numberText]);
        numberText--;
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    private void saveIngredients() {
        for (int i = 0; i <= numberText; i++) {
            bd.insertIngredient(nameRecipe,
                    textFieldIngredients[i].getText(),
                    textFieldValueIng[i].getText(),
                    (String) comboBox[i].getSelectedItem());
            textFieldIngredients[i].setText("");
            textFieldValueIng[i].setText("");
        }
        deleteTextField(numberText);
    }

    private void deleteTextField(int numberText){
        for(int i=numberText; i>0; i--){
            panel.remove(textFieldIngredients[i]);
            panel.remove(textFieldValueIng[i]);
            panel.remove(comboBox[i]);
        }
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    private String ingredientsIsEmpty() {
        for (int i = 0; i <= numberText; i++) {
            if (textFieldIngredients[i].getText().isEmpty() || textFieldValueIng[i].getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Название продукта не должно быть пустым. Введите название продукта",
                        "Сохранение рецепта в базе данных",
                        JOptionPane.INFORMATION_MESSAGE);
                return "null";
            }
        }
        return "yes";
    }
}
