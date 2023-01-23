package ru;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;
import java.util.List;

public class EditRecipeGUI {
    public JFrame frame;
    private JPanel panel = new JPanel();
    private JPanel panelRight = new JPanel();
    private JTextField textFieldNameRecipe = new JTextField(27);
    private JTextField textFieldIngredients = new JTextField(32);
    private JTextField textFieldValueIng = new JTextField(5);
    private JButton buttonAddIng = new JButton("Доб. ингридиент");
    private JButton buttonDeleteIng = new JButton("Удалить ингрид.");
    private JButton buttonDelete = new JButton("Удалить рецепт");
    private JButton buttonEditDescription = new JButton("Изм. описание");
    private JButton buttonEditCategory = new JButton("Изм. категорию");
    private JButton buttonEditNameRecipe = new JButton("Изм. название");
    private JButton buttonEditIngredient = new JButton("Изм. ингридиент");
    private JButton buttonBack = new JButton("Выйти в меню");
    private JLabel labelNameRecipe = new JLabel("Изменить название блюда:");
    private JLabel labelDescriptionRecipe = new JLabel("Редактировать описание:");
    private JLabel labelEdit = new JLabel("Редактирование ингридиентов :");
    private JLabel labelNameIngredient = new JLabel("Изменить название продукта:                                               ");
    private JLabel labelAmountIngredient = new JLabel("Кол-во:   ");
    private JLabel labelGram = new JLabel("Мл/Г: ");
    private JTextArea area = new JTextArea(10, 42);
    private JLabel labelCategory = new JLabel("Категория рецепта :");
    private String[] itemsCategory = {"Супы", "Мясо", "Рыба", "Салаты", "Гарниры", "Напитки"};
    private JComboBox comboBoxCategory = new JComboBox(itemsCategory);
    private String[] items = {"г", "шт", "мл", "кг", "банка", "упак"};
    private JComboBox comboBox = new JComboBox(items);
    private JScrollPane scroller;
    private JLabel labelListIngredients = new JLabel("Список ингридиентов :");
    private JPanel panelPut = new JPanel();
    private JLabel[] labelPut = new JLabel[18];
    private JLabel labelFirstMargin = new JLabel("****************************************************************************************");
    private JLabel labelLastMargin = new JLabel("****************************************************************************************");
    private String nameRecipe;
    private String textDescription;
    private ControlBDRecipes bd = new ControlBDRecipes();
    private List<String> list;
    private String category;

    public EditRecipeGUI(String nameRecipe) {
        this.nameRecipe = nameRecipe;
        editRecipe();
    }

    public void editRecipe() {
        frame = new JFrame("Редактировать рецепт");
        buttonEditDescription.setFocusPainted(false);
        buttonDelete.setFocusPainted(false);
        buttonEditIngredient.setFocusPainted(false);
        buttonEditNameRecipe.setFocusPainted(false);
        buttonEditCategory.setFocusPainted(false);
        buttonBack.setFocusPainted(false);
        buttonAddIng.setFocusPainted(false);
        buttonDeleteIng.setFocusPainted(false);

        listIngredients();
        panel.add(labelNameRecipe);
        textFieldNameRecipe.setText(nameRecipe);
        panel.add(textFieldNameRecipe);
        panel.add(labelDescriptionRecipe);
        descriptionRecipe();
        panel.add(labelListIngredients);
        panel.add(panelPut);

        panel.add(labelEdit);
        panel.add(labelNameIngredient);
        panel.add(labelAmountIngredient);
        panel.add(labelGram);
        panel.add(textFieldIngredients);
        panel.add(textFieldValueIng);
        panel.add(comboBox);

        panelRight.add(buttonDelete);
        panelRight.add(labelCategory);
        categoryRecipe();
        category = (String) comboBoxCategory.getSelectedItem();
        panelRight.add(comboBoxCategory);
        panelRight.add(buttonEditCategory);
        panelRight.add(buttonEditNameRecipe);
        panelRight.add(buttonEditDescription);
        panelRight.add(buttonEditIngredient);
        panelRight.add(buttonAddIng);
        panelRight.add(buttonDeleteIng);
        panelRight.add(buttonBack);

        panelRight.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelRight.setPreferredSize(new Dimension(170, 300));

        itemsCategory[0] = "Салаты";
        comboBoxCategory.setSelectedItem("Салатики");

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bd.deleteRecipe(nameRecipe);
                frame.dispose();
                AllRecipesGUI all = new AllRecipesGUI();
                all.lookRecipes();
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

        buttonEditNameRecipe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldNameRecipe.getText().equals(nameRecipe)) {
                    JOptionPane.showMessageDialog(frame,
                            "Названия РЕЦЕПТОВ совпадают! Измените название рецепта!",
                            "Изменение название рецепта",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (bd.updateNameRecipe(nameRecipe, textFieldNameRecipe.getText())) {
                    JOptionPane.showMessageDialog(frame,
                            "Изменение произошло успешно",
                            "Изменение название рецепта",
                            JOptionPane.INFORMATION_MESSAGE);
                    nameRecipe = textFieldNameRecipe.getText();
                } else JOptionPane.showMessageDialog(frame,
                        "Название не изменено",
                        "Изменение название рецепта",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.invalidate();
                frame.validate();
                frame.repaint();
            }
        });

        buttonEditDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (area.getText().equals(textDescription)) {
                    JOptionPane.showMessageDialog(frame,
                            "Описание рецептов совпадают! Измените название рецепта!",
                            "Изменение описания рецепта",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                if (bd.updateDescriptionRecipe(textDescription, area.getText())) {
                    JOptionPane.showMessageDialog(frame,
                            "Изменение произошло успешно",
                            "Изменение описания рецепта",
                            JOptionPane.INFORMATION_MESSAGE);
                    textDescription = area.getText();
                } else JOptionPane.showMessageDialog(frame,
                        "Изменение Не прошло",
                        "Изменение описания рецепта",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonEditIngredient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bd.updateIngredient(nameRecipe,
                        textFieldIngredients.getText(),
                        textFieldValueIng.getText(),
                        (String) comboBox.getSelectedItem());
                panelPut.removeAll();
                listIngredients();
                frame.invalidate();
                frame.validate();
                frame.repaint();
            }
        });
        buttonAddIng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bd.insertIngredient(nameRecipe,
                        textFieldIngredients.getText(),
                        textFieldValueIng.getText(),
                        (String) comboBox.getSelectedItem());
                panelPut.removeAll();
                listIngredients();
                frame.invalidate();
                frame.validate();
                frame.repaint();
            }
        });
        buttonDeleteIng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bd.deleteIngredient(nameRecipe, textFieldIngredients.getText());
                panelPut.removeAll();
                listIngredients();
                frame.invalidate();
                frame.validate();
                frame.repaint();
            }
        });
        buttonEditCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (category.equals(comboBoxCategory.getSelectedItem())) return;
                bd.updateCategory(nameRecipe, (String) comboBoxCategory.getSelectedItem());
                category = (String) comboBoxCategory.getSelectedItem();
            }
        });
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.EAST, panelRight);
        frame.setSize(650, 700);                    // Размер окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Закрытие формы "на крестик"
        frame.setResizable(false);                              // Редактировать форму запрещено
        frame.setLocationRelativeTo(null);                      // Позиция окна в центре экрана
        frame.setVisible(true);                                 // Отображение формы
    }

    private void categoryRecipe() {
        comboBoxCategory.setSelectedItem(bd.selectCategoryRecipe(nameRecipe));
    }

    private void descriptionRecipe() {
        scroller = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        area.append(bd.selectDescription(nameRecipe));
        textDescription = area.getText();
        panel.add(scroller);
    }

    private void listIngredients() {
        panelPut.setLayout(new BoxLayout(panelPut, BoxLayout.Y_AXIS));
        panelPut.add(labelFirstMargin);
        list = bd.selectRecipe(nameRecipe);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            labelPut[i] = new JLabel(list.get(i));
            panelPut.add(labelPut[i]);
        }
        panelPut.add(labelLastMargin);
    }
}
