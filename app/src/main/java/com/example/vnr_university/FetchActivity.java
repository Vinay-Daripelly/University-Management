package com.example.vnr_university;
import android.os.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.*;

@SuppressWarnings("ALL")
public class FetchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] queries= {"Display the titles of all borrowed books with their due date:",
            "Identify Students Who Have Not Borrowed Any Books:.",
            "Count the Number of Students Enrolled in Each Year:.",
            "List Books That Are Currently Available:",
            "Find the faculty names where the they have also written the book",
            "Find out the books borrowed which are published after 2015",
            "To display courses from the maximum enrolled to least enrolled .",
            "To retrieve the course names from the \"Computer Science\" department with credits more than 2 along with the faculty names who are teaching those courses.",
            "To list out all students who have been taught under the name \"Jane Doe\".",
            "To retrieve details of students who borrowed the book \"Database Management Systems,\"."};
    TextView  e1,textViewf2;
    int a=0;
    Connection conn;
    String conRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        textViewf2 = findViewById(R.id.textView);
        Spinner  spinner=findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, queries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: " + position+queries[position], Toast.LENGTH_SHORT).show();
        a=position+1;
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
    public void getConnection(View v) {
                try {
                    ConnectionHelper ch=new ConnectionHelper();
                    conn=ch.connectionMethod();
                    if(conn!=null)
                    {
                        String query="select name  from Student";
                        Statement st=conn.createStatement();
                        ResultSet rs;
                        StringBuffer str1=new StringBuffer();
                      switch (a)
                        {
                            case 1:
                            {
                                Toast.makeText(this,"case 1",Toast.LENGTH_SHORT).show();
                                query="SELECT l.title, b.borrow_date, b.return_date\n" +
                                        "FROM Library l\n" +
                                        "INNER JOIN Borrowing b ON l.book_id = b.book_id\n" +
                                        "WHERE b.return_date IS NULL;;\n";
                                         rs=st.executeQuery(query);

                                        str1=new StringBuffer();
                                while(rs.next())
                                {
                                    str1.append(rs.getString(1)+"   "+rs.getDate(2)+rs.getDate(3)+"\n");

                                }
                                break;

                            }
                            case 2:
                            {
                                query="SELECT Student.student_id, Student.name\n" +
                                        "FROM Student\n" +
                                        "LEFT JOIN Borrowing ON Student.student_id = Borrowing.student_id\n" +
                                        "WHERE Borrowing.borrow_id IS NULL;";
                                rs=st.executeQuery(query);
                                str1=new StringBuffer();

                                while(rs.next())
                                {
                                    str1.append(rs.getInt(1)+rs.getString(2)+"\n");
                                }
                                break;

                            }
                            case 3:
                            {
                                query="SELECT enrollment_year, COUNT(student_id) AS student_count\n" +
                                        "FROM Student\n" +
                                        "GROUP BY enrollment_year;";
                                rs=st.executeQuery(query);
                                str1=new StringBuffer();

                                while(rs.next())
                                {
                                    str1.append(rs.getString(1)+"   "+rs.getInt(2)+"\n");
                                }
                                break;
                            }
                            case 4:
                            {
                                query="SELECT book_id, title, author\n" +
                                        "FROM Library\n" +
                                        "WHERE availability=1;";
                                 rs=st.executeQuery(query);
                                 str1=new StringBuffer();
                                while(rs.next())
                                {
                                    str1.append(rs.getInt(1)+"   "+rs.getString(2)+"  "+rs.getString(3)+"\n");
                                }
                                break;

                            }
                            case 5:
                            {
                                query="SELECT Library.book_id, Library.title, Library.author, Faculty.name AS faculty_name  FROM Library\n" +
                                        "JOIN Borrowing ON Library.book_id = Borrowing.book_id\n" +
                                        "JOIN Student ON Borrowing.student_id = Student.student_id\n" +
                                        "JOIN Enrollment ON Student.student_id = Enrollment.student_id\n" +
                                        "JOIN Courses ON Enrollment.course_id = Courses.course_id\n" +
                                        "JOIN Faculty ON Courses.faculty_id = Faculty.faculty_id\n" +
                                        "WHERE Library.author = Faculty.name;\n";
                                 rs=st.executeQuery(query);
                               str1=new StringBuffer();

                                while(rs.next())
                                {

                                    str1.append(rs.getInt(1)+" "+rs.getString(2)+rs.getString(3));
                                }

                                break;

                            }
                            case 6:
                            {
                                query="SELECT Borrowing.borrow_id, Library.title, Library.author, Library.publication_year, Borrowing.borrow_date, Borrowing.return_date\n" +
                                        "FROM Borrowing\n" +
                                        "JOIN Library ON Borrowing.book_id = Library.book_id\n" +
                                        "WHERE Library.publication_year>2015;";
                                 rs=st.executeQuery(query);
                                 str1=new StringBuffer();

                                while(rs.next())
                                {

                                    str1.append(rs.getInt(1)+" "+rs.getString(2)+"  "+rs.getString(3)+rs.getString(4)+" "+rs.getDate(5)+" "+rs.getDate(6)+ "\n");
                                }
                                break;

                            }
                            case 7:
                            {
                                query="SELECT Courses.course_id, Courses.course_name, COUNT(Enrollment.enrollment_id) AS enrollment_count\n" +
                                        "FROM Courses\n" +
                                        "LEFT JOIN Enrollment ON Courses.course_id = Enrollment.course_id\n" +
                                        "GROUP BY Courses.course_id, Courses.course_name\n" +
                                        "ORDER BY enrollment_count DESC;";
                                 rs=st.executeQuery(query);
                                 str1=new StringBuffer();

                                while(rs.next())
                                {

                                    str1.append(rs.getInt(1)+"   "+rs.getString(2)+" "+rs.getInt(3)+"\n");
                                }
                                break;

                            }
                            case 8:
                            {
                                query= "SELECT Courses.course_name, Faculty.name AS faculty_name\n" +
                                        "FROM Courses\n" +
                                        "JOIN Faculty ON Courses.faculty_id = Faculty.faculty_id\n" +
                                        "WHERE Courses.department = 'Computer Science' AND Courses.credits > 2;";
                                 rs=st.executeQuery(query);
                                 str1=new StringBuffer();

                                while(rs.next())
                                {
                                    str1.append(rs.getString(1)+"  "+rs.getString(2)+"\n");
                                }
                                break;
                            }
                            case 9:
                            {
                                query="SELECT DISTINCT Student.student_id, Student.name, Student.email, Student.major, Student.enrollment_year\n" +
                                        "   FROM Student\n" +
                                        "    JOIN Enrollment ON Student.student_id = Enrollment.student_id\n" +
                                        "    JOIN Courses ON Enrollment.course_id = Courses.course_id\n" +
                                        "    JOIN Faculty ON Courses.faculty_id = Faculty.faculty_id\n" +
                                        "    WHERE Faculty.name = 'Jane Doe';";
                                 rs=st.executeQuery(query);
                                 str1=new StringBuffer();

                                while(rs.next())
                                {
                                    str1.append(rs.getInt(1)+"   "+rs.getString(2)+"   "+rs.getString(3)+"   "+rs.getString(4)+"   "+rs.getString(5)+"\n");
                                }
                                break;

                            }
                            case 10:
                            {
                                query="SELECT DISTINCT Student.student_id, Student.name, Student.email, Student.major, Borrowing.borrow_date, Borrowing.return_date\n" +
                                        "FROM Student\n" +
                                        "JOIN Borrowing ON Student.student_id = Borrowing.student_id\n" +
                                        "JOIN Library ON Borrowing.book_id = Library.book_id\n" +
                                        "WHERE Library.title = 'Database Management Systems';";
                                rs=st.executeQuery(query);
                                str1=new StringBuffer();

                                while(rs.next())
                                {

                                    str1.append(rs.getInt(1)+"   "+rs.getString(2)+"   "+rs.getString(3)+"   "+rs.getString(4)+"   "+rs.getDate(5)+"   "+rs.getDate(6)+"\n");
                                }
                                break;

                            }
                        }
                        textViewf2.setText(str1);
                        textViewf2.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        conRes="check Connection";
                    }
                }
                catch (Exception ex)
                {
                    System.out.println(conRes);
                }
        }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
