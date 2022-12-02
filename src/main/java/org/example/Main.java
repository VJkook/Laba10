package org.example;

import java.sql.*;

public class Main {
    static Connection connection = null;
    static Statement statement = null;
    static ResultSet result = null;

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/task10",
                    "postgres", "Cde#Vfr$");

            statement = connection.createStatement();

            System.out.println("Задание  : Вывести список студентов, сдавших определенный предмет, на оценку выше 3 ");
            St_mark_3(statement);
            System.out.println();

            System.out.println("Задание : Посчитать средний бал по определенному предмету");
            Avg_sudj(statement);
            System.out.println();

            System.out.println("Задание : Посчитать средний балл по определенному студенту ");
            Avg_stud_mark(statement);
            System.out.println();

            System.out.println("Задание : Найти три предмета, которые сдали наибольшее количество студентов");
            subj_cross(statement);
            System.out.println();

            System.out.println("Задание : Найти отличника");
            St_mark_5(statement);
            System.out.println();

        }
        catch (SQLException e) {
            System.out.println("No connection to database");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Вывести список студентов, сдавших определенный предмет, на оценку выше 3

    public static void St_mark_3(Statement statement) throws SQLException {
        result= statement.executeQuery("select p.ID_Student, st.Name, sb.Subject_Name, p.Mark\n" +
                "from progress p inner join Students st on p.ID_Student=st.ID_Student inner join Subjects sb on p.ID_Subject=sb.ID_Subject\n" +
                "where p.Mark > 3 and sb.Subject_Name = 'Физкультура'\n" +
                "order by p.ID_Student;"
        );

        while (result.next()) {
            String studName = result.getString("Name");
            String subjName = result.getString("Subject_Name");
            int Mark = result.getInt("Mark");

            System.out.println( studName+" "+subjName+" "+Mark);
        }
    }

    //Посчитать средний бал по определенному предмету - Управление данными (чтобы поменять сменить subject_id = 2 на

    public static void Avg_sudj(Statement statement) throws SQLException{
        result = statement.executeQuery("select sb.Subject_Name, avg(p.Mark)\n" +
                "from Progress p inner join Subjects sb on p.ID_Subject=sb.ID_Subject\n" +
                "group by sb.Subject_Name\n" +
                "having sb.Subject_Name = 'Физкультура';"
        );

        while (result.next()) {
            String subjName = result.getString("Subject_Name");
            float avgMark = result.getFloat("avg");

            System.out.println(subjName+" ср. оценка: "+avgMark);
        }
    }

    // Посчитать средний балл по определенному студенту
    public static void Avg_stud_mark(Statement statement) throws SQLException {
        result = statement.executeQuery("select st.Name, avg(p.Mark)\n" +
                "from Progress p inner join Students st on p.ID_Student=st.ID_Student\n" +
                "group by st.Name\n" +
                "having st.Name = 'Иван';"
        );

        while (result.next()) {
            String studName = result.getString("Name");
            float avgMark = result.getFloat("avg");

            System.out.println(studName+" ср. оценка: "+avgMark);
        }
    }


    //Найти три предмета, которые сдали наибольшее количество студентов

    public static void subj_cross(Statement statement) throws SQLException {
        result = statement.executeQuery("select sb.ID_Subject as id, sb.Subject_Name, count(*) as passed\n" +
                "from Progress p inner join Subjects sb on p.ID_Subject=sb.ID_Subject\n" +
                "where p.Mark > 2\n" +
                "group by sb.ID_Subject\n" +
                "order by count(*) desc\n" +
                "limit 3;"
        );

        while (result.next()) {
            String Subject_Name = result.getString("Subject_Name");

            System.out.println(Subject_Name);
        }
    }

    public static void St_mark_5(Statement statement) throws SQLException {
        result = statement.executeQuery("select st.Name, sb.Subject_Name\n" +
                "from Progress p inner join Students st on p.ID_Student=st.ID_Student inner join Subjects sb on p.ID_Subject=sb.ID_Subject\n" +
                "where p.ID_Student in (\n" +
                "select p.ID_Student\n" +
                "from Progress p inner join Students st on p.ID_Student=st.ID_Student inner join Subjects sb on p.ID_Subject=sb.ID_Subject\n" +
                "group by p.ID_Student, p.mark, st.Name\n" +
                "having p.mark = 5 and count(p.ID_Student) = (select count( distinct ID_Subject) from Progress)\n" +
                "order by p.ID_Student desc)\n" +
                "order by st.Name limit 4 offset 1;"
        );

        while (result.next()) {
            String Name = result.getString("Name");
            //System.out.println();
            String Sb_name = result.getString("Subject_Name");
            System.out.println(Name +" "+Sb_name);
        }
    }


}