import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.opencsv.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;


public class ContinueTesting {
    ChromeDriver chromeDriver;
    @BeforeMethod
    public void SetUp() {
        // Khởi tạo trình duyệt
        WebDriverManager.chromedriver().setup(); //kiểm tra v. chrome trên máy và set up vào
        chromeDriver = new ChromeDriver();
        // API của WebDriver
        chromeDriver.navigate().refresh();
        chromeDriver.navigate().back();
        chromeDriver.navigate().forward();
    }
    public void logIn () throws Exception {
        chromeDriver.manage().window().maximize();
        chromeDriver.get("http://localhost/orangehrm-4.5/symfony/web/index.php/admin/saveSystemUser");
        CSVReader reader = new CSVReader(new FileReader("C://Users//admin//IdeaProjects//ExcelTesting//src//test//resources//dataLogin.csv"));
        String csvCell[];
        while ((csvCell = reader.readNext()) != null)
        {
            String UserName = csvCell[0];
            String Pass = csvCell[1];

            chromeDriver.findElement(By.xpath("//*[@id=\"txtUsername\"]")).click();
            chromeDriver.findElement(By.xpath("//*[@id=\"txtUsername\"]")).sendKeys(UserName);
            chromeDriver.findElement(By.xpath("//*[@id=\"txtPassword\"]")).click();
            chromeDriver.findElement(By.xpath("//*[@id=\"txtPassword\"]")).sendKeys(Pass);
            chromeDriver.findElement(By.xpath("//*[@id=\"btnLogin\"]")).click();

        }
    }
    @Test
    public void addUser() throws Exception {
        chromeDriver.manage().window().maximize();
        chromeDriver.get("http://localhost/orangehrm-4.5/symfony/web/index.php/admin/saveSystemUser");
        logIn();
        CSVReader reader = new CSVReader(new FileReader("C://Users//admin//IdeaProjects//ExcelTesting//src//test//resources//data.csv"));
        String csvCell[];
        CSVWriter writer = new CSVWriter(new FileWriter("C://Users//admin//IdeaProjects//ExcelTesting//src//test//resources//result.csv"));
        String[] header = {"User Role", "Employee Name", "Username","Status", "Password","Confirm Password","Expected Output","Actual Output","Status Of Test Case"};
        writer.writeNext(header);
        boolean check = true;
        while ((csvCell = reader.readNext()) != null)
        {
            if(check){
                check=false;
                continue;
            }
            String Role = csvCell[0];
            String EmployeeName = csvCell[1];
            String UserName = csvCell[2];
            String UserStatus = csvCell[3];
            String Pass = csvCell[4];
            String ConPass = csvCell[5];
            String Message = csvCell[6];

            WebElement UserRole = chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userType\"]"));
            Select select = new Select(UserRole);
            select.selectByVisibleText(Role);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_employeeName_empName\"]")).sendKeys(EmployeeName);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userName\"]")).sendKeys(UserName);
            WebElement Status = chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_status\"]"));
            Select select1 = new Select(Status);
            select1.selectByVisibleText(UserStatus);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_password\"]")).sendKeys(Pass);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_confirmPassword\"]")).sendKeys(ConPass);
            chromeDriver.findElement(By.xpath("//*[@id=\"btnSave\"]")).click();
            Thread.sleep(5000);


            String EmployeeNameMessage=chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[2]/span")).getText();
            String UsernameMessage=chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[3]/span")).getText();
            String PasswordMessage=chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[6]/span")).getText();
            String ConfirmPassword= chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[7]/span")).getText();

            if(!EmployeeNameMessage.isEmpty()){
                csvCell[7] = EmployeeNameMessage;
            } else if (!UsernameMessage.isEmpty()) {
                csvCell[7] = UsernameMessage;
            } else if (!PasswordMessage.isEmpty()) {
                csvCell[7] = PasswordMessage;
            } else if (!ConfirmPassword.isEmpty()) {
                csvCell[7] = ConfirmPassword;
            }
            if(Message.equals(csvCell[7])){
                csvCell[8]="PASS";
            }
            else{
                csvCell[8]="FAIL";
            }
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA");
            writer.writeNext(csvCell);

            Thread.sleep(5000);

            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_employeeName_empName\"]")).clear();
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userName\"]")).clear();
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_password\"]")).clear();
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_confirmPassword\"]")).clear();
            EmployeeNameMessage="";
            UsernameMessage="";
            PasswordMessage="";
            ConfirmPassword="";

        }
        writer.close();
    }

    @AfterMethod
    public void CleanUp() {

        chromeDriver.quit();
    }

}

