package ru;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControlBDRecipes {
    private static final String URL = "jdbc:postgresql://localhost:5432/cookbook_bd";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "Postgres";
    private static final Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private List<String> namesRecipes;

    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String insertNameRecipe(String nameRecipe, String nameCategory) {
        String textError = "";
        if (validatorNameRecipe(nameRecipe)) return "no";
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO recipe(\"name\",\"category\") VALUES (?,?)");
            preparedStatement.setString(1, nameRecipe);
            preparedStatement.setString(2, nameCategory);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            textError = e.getMessage();
        }
        return textError;
        // СДЕЛАТЬ ПРОВЕРКУ НА СТРОЧНЫЕ БУКВЫ, Т.е. вводить только буквы, в КОЛ-ВО ЦИФРЫ
        // ИСПОЛЬЗОВАТЬ РЕГУЛЯРЫНЕ ВЫРАЖЕНИЯ
    }

    public boolean validatorNameRecipe(String nameRecipe) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT \"name\" FROM recipe WHERE \"name\" =?");
            preparedStatement.setString(1, nameRecipe);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.wasNull() ? null : true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String insertIngredient(String nameRecipe, String nameIng, String unit, String meaUnit) {
        String textError = "";
        int id_recipe = getIdRecipeByName(nameRecipe);
        if (id_recipe == 0) return "No_Ing";
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO ingredient(id_recipe, \"name\", unit, measurement_unit) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, id_recipe);
            preparedStatement.setString(2, nameIng);
            preparedStatement.setString(3, unit);
            preparedStatement.setString(4, meaUnit);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            textError = e.getMessage();
        }
        return textError;
    }

    public String insertDescription(String nameRecipe, String DescRecipe) {
        String textError = "good";
        int id_recipe = getIdRecipeByName(nameRecipe);
        if (id_recipe == 0) return "No_Desc";
        try {
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO description_recipe(id_recipe, description) VALUES (?, ?)");
            preparedStatement.setInt(1, id_recipe);
            preparedStatement.setString(2, DescRecipe);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            textError = e.getMessage();
        }
        return textError;
    }

    private int getIdRecipeByName(String nameRecipe) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT id_recipe FROM recipe WHERE \"name\"=?");
            preparedStatement.setString(1, nameRecipe);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("id_recipe");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public boolean updateNameRecipe(String nameRecipe, String newNameRecipe) {
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE recipe SET \"name\"=? WHERE \"name\"=? RETURNING \"name\"");
            preparedStatement.setString(1, newNameRecipe);
            preparedStatement.setString(2, nameRecipe);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.wasNull() ? null : true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateDescriptionRecipe(String DescRecipe, String newDescRecipe) {
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE description_recipe SET description=? WHERE description=? RETURNING description");
            preparedStatement.setString(1, newDescRecipe);
            preparedStatement.setString(2, DescRecipe);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.wasNull() ? null : true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateIngredient(String nameRecipe, String nameIng, String unit, String meaUnit) {
        int id_recipe = getIdRecipeByName(nameRecipe);
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE ingredient SET \"name\"=?, unit=?, measurement_unit=?  WHERE id_recipe=? AND \"name\"=?");
            preparedStatement.setString(1, nameIng);
            preparedStatement.setString(2, unit);
            preparedStatement.setString(3, meaUnit);
            preparedStatement.setInt(4, id_recipe);
            preparedStatement.setString(5, nameIng);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCategory(String nameRecipe, String nameCategory) {
        int id_recipe = getIdRecipeByName(nameRecipe);
        try {
            preparedStatement = connection.prepareStatement(
                    "UPDATE recipe SET \"category\"=? WHERE id_recipe=?");
            preparedStatement.setString(1, nameCategory);
            preparedStatement.setInt(2, id_recipe);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteRecipe(String nameRecipe) {
        try {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM recipe WHERE \"name\"=?");
            preparedStatement.setString(1, nameRecipe);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteIngredient(String nameRecipe, String nameIng) {
        int id_recipe = getIdRecipeByName(nameRecipe);
        try {
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM ingredient WHERE \"name\"=? AND id_recipe=?");
            preparedStatement.setString(1, nameIng);
            preparedStatement.setInt(2, id_recipe);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> selectNamesRecipe(String nameCategory) {
        namesRecipes = new ArrayList<>();
        try {
            if (nameCategory == "Все") {
                preparedStatement = connection.prepareStatement(
                        "SELECT \"name\" FROM recipe ORDER BY \"name\" ASC");
            } else {
                preparedStatement = connection.prepareStatement(
                        "SELECT \"name\" FROM recipe WHERE \"category\"= ? ORDER BY \"name\" ASC");
                preparedStatement.setString(1, nameCategory);
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                namesRecipes.add(resultSet.getString("name"));
            }
            return namesRecipes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> selectRecipe(String nameRecipe) {
        List<String> list = new ArrayList<>();
        StringBuilder strBuilder = new StringBuilder();
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT ingredient.\"name\", unit, measurement_unit\n" +
                            "FROM recipe JOIN ingredient ON recipe.id_recipe = ingredient.id_recipe\n" +
                            "WHERE recipe.name = ?");
            preparedStatement.setString(1, nameRecipe);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                strBuilder.append(resultSet.getString("name") + " - ");
                strBuilder.append(resultSet.getString("unit") + " ");
                strBuilder.append(resultSet.getString("measurement_unit"));
                list.add(String.valueOf(strBuilder));
                strBuilder.delete(0, strBuilder.length());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public String selectDescription(String nameRecipe) {
        int id_recipe = getIdRecipeByName(nameRecipe);
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT description FROM description_recipe WHERE id_recipe=?");
            preparedStatement.setInt(1, id_recipe);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("description");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "пусто";
    }

    public String selectCategoryRecipe(String nameRecipe) {
        int id_recipe = getIdRecipeByName(nameRecipe);
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT \"category\" FROM recipe WHERE id_recipe=?");
            preparedStatement.setInt(1, id_recipe);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("category");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
