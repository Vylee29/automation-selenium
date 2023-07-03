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
    Integer isPassed = 0;

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
        CSVReader reader = new CSVReader(new FileReader("C://Users//admin//IdeaProjects//ExcelTesting//src//test//resources//Book1.csv"));
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
        CSVReader reader = new CSVReader(new FileReader("C://Users//admin//IdeaProjects//ExcelTesting//src//test//resources//Book3.csv"));
        String csvCell[];
        CSVWriter writer = new CSVWriter(new FileWriter("C://Users//admin//IdeaProjects//ExcelTesting//src//test//resources//result.csv"));
        String[] header = { "Employee Name", "Username", "Password","Confirm Password","Expected Output","Actual Output","Status"};
        writer.writeNext(header);
        while ((csvCell = reader.readNext()) != null)
        {
            String EmployeeName = csvCell[0];
            String UserName = csvCell[1];
            String Pass = csvCell[2];
            String ConPass = csvCell[3];
            String Message = csvCell[4];

            WebElement UserRole = chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userType\"]"));
            Select select = new Select(UserRole);
            select.selectByVisibleText("ESS");

            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_employeeName_empName\"]")).sendKeys(EmployeeName);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userName\"]")).sendKeys(UserName);
            WebElement Status = chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_status\"]"));
            Select select1 = new Select(Status);
            select1.selectByVisibleText("Enabled");
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_password\"]")).sendKeys(Pass);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_confirmPassword\"]")).sendKeys(ConPass);
            chromeDriver.findElement(By.xpath("//*[@id=\"btnSave\"]")).click();
            Thread.sleep(5000);

            // add data to csv
            writer.writeNext(csvCell);

            String EmployeeNameMessage=chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[2]/span")).getText();
            String UsernameMessage=chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[3]/span")).getText();
            String PasswordMessage=chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[6]/span")).getText();
            String ConfirmPassword= chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[7]/span")).getText();
            //WebElement notice6 = chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[6]/span"));
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//            if(!EmployeeNameMessage.isEmpty()){
//                csvCell[]
//                if(EmployeeNameMessage.equals(Message))
//            }

            writer.close();
            Thread.sleep(5000);

//
//            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_employeeName_empName\"]")).clear();
//            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userName\"]")).clear();
//            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_password\"]")).clear();
//            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_confirmPassword\"]")).clear();

        }
    }



    @AfterMethod
    public void CleanUp() {
        System.out.println("After Method");
    }

}

