package AddJobtitle;
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


public class AddJobTItle {
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
        chromeDriver.get("http://localhost/orangehrm-4.5/symfony/web/index.php/admin/saveJobTitle");
        CSVReader reader = new CSVReader(new FileReader("D://CNTT//kiemthuphanmem//automation-selenium//src//test//resources//Book1.csv"));
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
        chromeDriver.get("http://localhost/orangehrm-4.5/symfony/web/index.php/admin/saveJobTitle");
        logIn();
        CSVReader reader = new CSVReader(new FileReader("D://CNTT//kiemthuphanmem//automation-selenium//src//test//resources//addjobtitle.csv"));
        String csvCell[];
        CSVWriter writer = new CSVWriter(new FileWriter("D://CNTT//kiemthuphanmem//automation-selenium//src//test//resources//result//addJobtitleResult.csv"));
        boolean check =true;
        while ((csvCell = reader.readNext()) != null)
        {
            if(check){
                check=false;
                writer.writeNext(csvCell);
                continue;
            }
            String JobTitle = csvCell[2];
            String Description = csvCell[3];
            String Specification = csvCell[4];
            String Note = csvCell[5];
            String Message = csvCell[6];

            chromeDriver.findElement(By.xpath("//*[@id=\"jobTitle_jobTitle\"]")).sendKeys(JobTitle);
            chromeDriver.findElement(By.xpath("//*[@id=\"jobTitle_jobDescription\"]")).sendKeys(Description);
            if(!Specification.equals("")) {
                chromeDriver.findElement(By.xpath("//*[@id=\"jobTitle_jobSpec\"]")).sendKeys(Specification);
            }chromeDriver.findElement(By.xpath("//*[@id=\"btnSave\"]")).sendKeys(Note);

         chromeDriver.findElement(By.xpath("//*[@id=\"btnSave\"]")).click();
            System.out.println("asssssssdasfds");

            String hhh=chromeDriver.findElement(By.xpath("//*[@id=\"frmList_ohrmListComponent\"]/div[2]")).getText();
            System.out.println(hhh);
            System.out.println("asssssssdasfds");
            Thread.sleep(1000);

            // add data to csv
            writer.writeNext(csvCell);

            //WebElement notice6 = chromeDriver.findElement(By.xpath("//*[@id=\"frmSystemUser\"]/fieldset/ol/li[6]/span"));
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//            if(!EmployeeNameMessage.isEmpty()){
//                csvCell[]
//                if(EmployeeNameMessage.equals(Message))
//            }

            Thread.sleep(1000);
            chromeDriver.findElement(By.xpath("//*[@id=\"jobTitle_jobTitle\"]")).clear();
            chromeDriver.findElement(By.xpath("//*[@id=\"jobTitle_jobDescription\"]")).clear();
            chromeDriver.findElement(By.xpath("//*[@id=\"jobTitle_jobSpec\"]")).clear();
            chromeDriver.findElement(By.xpath("//*[@id=\"jobTitle_note\"]")).clear();

        }
        writer.close();

    }



    @AfterMethod
    public void CleanUp() {
        System.out.println("After Method");
    }
}
