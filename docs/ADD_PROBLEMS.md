# Steps to adding a problem to the site
## Step 1: Create problem statement in markdown
- Create a statement that explains the problem statement using markdown, example below:
```md
This question involves the implementation of the Classroom class which represents a single company’s cellphone phone availability and prices.

In addition, the Student class is already written for you like so

\```
public class Student {
   private String name;
   private int grade; // 0 - 100
   
   public int getGrade() { /* implementation not shown */ }
   public void setGrade(int grade) { /* implementation not shown */ }

  public String getName() { /* implementation not shown */ }
  public void setName(String name) { /* implementation not shown */ }

}
\```

You will need to set up a classroom for Mr. Mortenson in the classroom class

\```
public class Classroom {
   // no duplicate names in this list
   private ArrayList<Student> students;

   /* constructor not shown, constructor initializes student list */

   public void addStudent (Student student) {
         /* code for part a */
   } 

   public ArrayList<Student> getGoodStudents (int cutoff) {
        /* code for part b */
   }
}
\```

Write the code for the addStudent method. If a student with the name is not already in the student’s list, the student should be added to the list. Otherwise, the existing student should keep its place in the list, and its grade should be updated to reflect that which was passed into the function.
\```
public void addStudent (Student student) {
    // YOUR CODE IS INSERTED HERE
}
\```
```
- Place this file in src/main/resources/javatemplates

## Step 2: Write code that tests your user's solution
- This code should have a variable to count how many points/testcases the user got in their solution
- Use special variables to denote things --> use ``{{ classname }}`` whereever you would put the name of the class, use ``{{ answer }}`` where the user's answer should go, use ``{{ specialcode }}`` when printing how many test cases user got (see example)
- In the example below, take special note of the main method, which is wehre your code to test the user's solutin goes (you must have the println statement at the end to denote how many testcases/points the user got)
```java
import java.util.ArrayList;

public class {{ classname }} {
   public static class Student {
       private String name;
       private int grade; // 0 - 100

       public int getGrade() { return this.grade; }
       public void setGrade(int grade) { this.grade = grade; }

      public String getName() { return this.name; }
      public void setName(String name) { this.name = name; }
      
      public Student(String name, int grade) {
         this.name = name;
         this.grade = grade;
      }

   }


   // no duplicate names in this list
   private ArrayList<Student> students;

   public {{ classname }}() {
      students = new ArrayList<Student>();
   }

   public void addStudent (Student student) {
         {{ answer }}
   } 
   
   public static void main (String[] args) {
    int points = 0;
    {{ classname }} classr = new {{ classname }}();

    classr.addStudent(new Student("Rohan", 95));
    if (classr.students.get(0).getName().equals("Rohan")) points++;
    classr.addStudent(new Student("Andrew", 90));
    if (classr.students.get(1).getName().equals("Andrew")) points++;
    classr.addStudent(new Student("Rohan", 100));
    if (classr.students.size() == 2) points++;
    if (classr.students.get(0).getGrade() == 100) points++;
    
    System.out.println("{{ specialcode }}-" + points);

   }
}
```
- Place this in the src/main/resources/javatemplates folder with an extension of .javat

## Step 3: Add categories to the java (will be moved to the yaml file)
- Make sure the java file in mvc/ModelInit.java contains all the categories you would like to use on this line (otherwise add more)
```java
String[] categories = {"2D Arrays", "Arrays", "ArrayLists"};
```

## Step 4: Add your problem to the yaml file
- At the end of the file src/main/resources/Problems.yaml, you need to add details about your problem
- This includes number of possible points/testcases, filename of description (your markdown file), filename of your javatemplate, level number (MUST BE UNIQUE), and categories
- See the example below, you would add something like this to the end of the yaml
```yaml
---

number: 8
name: Classroom FRQ Part B
problem: ClassroomPartB.javat
categories: [ArrayLists]
description: ClassroomPartB.md
testcases: 2
```
