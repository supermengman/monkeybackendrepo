This question involves the implementation of the Classroom class which represents a single company’s cellphone phone availability and prices.

In addition, the Student class is already written for you like so

```
public class Student {
   private String name;
   private int grade; // 0 - 100
   
   public int getGrade() { /* implementation not shown */ }
   public void setGrade(int grade) { /* implementation not shown */ }

  public String getName() { /* implementation not shown */ }
  public void setName(String name) { /* implementation not shown */ }

}
```

You will need to set up a classroom for Mr. Mortenson in the classroom class

```
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
```

Write the code for the addStudent method. If a student with the name is not already in the student’s list, the student should be added to the list. Otherwise, the existing student should keep its place in the list, and its grade should be updated to reflect that which was passed into the function.
```
public void addStudent (Student student) {
    // YOUR CODE IS INSERTED HERE
}
```
