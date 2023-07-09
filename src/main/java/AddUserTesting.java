import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.opencsv.*;

import java.io.FileReader;
import java.io.FileWriter;


public class AddUserTesting {
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
        CSVReader reader = new CSVReader(new FileReader("src/test/resources/data/dataLogin.csv"));
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
        CSVReader reader = new CSVReader(new FileReader("src/test/resources/data/data.csv"));
        String csvCell[];
        CSVWriter writer = new CSVWriter(new FileWriter("src/test/resources/result//result.csv"));
        String[] header = {"Num.","User Role", "Employee Name", "Username","Status", "Password","Confirm Password","Expected Output","Actual Output","Status Of Test Case"};
        writer.writeNext(header);
        boolean check = true;
        while ((csvCell = reader.readNext()) != null) {
            if (check) {
                check = false;
                continue;
            }
            // Gán giá trị thuộc tưừng dòng trong file
            String Role = csvCell[1];
            String EmployeeName = csvCell[2];
            String UserName = csvCell[3];
            String UserStatus = csvCell[4];
            String Pass = csvCell[5];
            String ConPass = csvCell[6];
            String Message = csvCell[7];

            // Điền vào tất cả các field
            WebElement UserRole = chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userType\"]"));
            Select select = new Select(UserRole);
            select.selectByVisibleText(Role);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_employeeName_empName\"]")).click();
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_employeeName_empName\"]")).sendKeys(EmployeeName);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userName\"]")).click();
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userName\"]")).sendKeys(UserName);
            WebElement Status = chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_status\"]"));
            Select select1 = new Select(Status);
            select1.selectByVisibleText(UserStatus);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_password\"]")).click();
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_password\"]")).sendKeys(Pass);
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_confirmPassword\"]")).click();
            chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_confirmPassword\"]")).sendKeys(ConPass);
            Thread.sleep(5000);
            chromeDriver.findElement(By.xpath("//*[@id=\"btnSave\"]")).click();
            Thread.sleep(1000);

            String currUrl = chromeDriver.getCurrentUrl();
            if (currUrl.equals("http://localhost/orangehrm-4.5/symfony/web/index.php/admin/viewSystemUsers"))
            {
                String SuccessMsg = chromeDriver.findElement(By.xpath("//*[@id=\"frmList_ohrmListComponent\"]/div[2]")).getText();
                if (!SuccessMsg.isEmpty()) {
                    csvCell[8] = SuccessMsg;
                }

                chromeDriver.findElement(By.xpath("//*[@id=\"btnAdd\"]")).click();

            } else {
                // Check thông báo ở các field
                String EmployeeNameMessage = chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[2]/span")).getText();
                String UsernameMessage = chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[3]/span")).getText();
                String PasswordMessage = chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[6]/span")).getText();
                String ConfirmPassword = chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[7]/span")).getText();


                if (!EmployeeNameMessage.isEmpty()) {
                    csvCell[8] = EmployeeNameMessage;
                } else if (!UsernameMessage.isEmpty()) {
                    csvCell[8] = UsernameMessage;
                } else if (!PasswordMessage.isEmpty()) {
                    csvCell[8] = PasswordMessage;
                } else if (!ConfirmPassword.isEmpty()) {
                    csvCell[8] = ConfirmPassword;
                }


                chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_employeeName_empName\"]")).clear();
                chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_userName\"]")).clear();
                chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_password\"]")).clear();
                chromeDriver.findElement(By.xpath("//*[@id=\"systemUser_confirmPassword\"]")).clear();

                EmployeeNameMessage = "";
                UsernameMessage = "";
                PasswordMessage = "";
                ConfirmPassword = "";

            }
            if (Message.equals(csvCell[8])) {
                csvCell[9] = "PASS";
            } else {
                csvCell[9] = "FAIL";
            }
            writer.writeNext(csvCell);

        }

        writer.close();
    }

    @AfterMethod
    public void CleanUp() {

        chromeDriver.quit();
    }

}

