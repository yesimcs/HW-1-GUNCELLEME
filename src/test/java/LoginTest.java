
    import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
    import org.testng.annotations.AfterMethod;
    import org.testng.annotations.BeforeMethod;
    import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;
    import java.util.ArrayList;
    import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

    public class LoginTest {

        public WebDriver chromeDriver;

        @BeforeMethod
        public void baslangic(){

            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
            chromeDriver = new ChromeDriver();
            chromeDriver.manage().window().maximize(); //tam ekran olmasını sağlıyor.
        }

        @AfterMethod
        public void bitis(){
            chromeDriver.quit();
        }

        @Test

        public void LoginTestChrome() {

            chromeDriver.get("https://www.trendyol.com/"); //HW 1. MADDE Go to https://www.trendyol.com/

            String currentUrl = chromeDriver.getCurrentUrl();
            if (currentUrl.equals("https://www.trendyol.com/")) {
                System.out.println("Ana sayfa başarılı bir şekilde açıldı.");
            } else {
                System.out.println("Ana sayfa açılmadı.");
            }
            //HW 2. MADDE Check that the main page is opened.
            //2.madde için daha kesin bir kontrol yolu var mı? yoksa Bu yöntem iyi mi?

            WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
            try {
                WebElement cerezKabulEtButonu = wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
                cerezKabulEtButonu.click();

                WebElement girisYap = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[.='Giriş Yap']")));
                girisYap.click();

            } catch (TimeoutException e) {
                System.out.println("Öğe tıklanabilir hale gelmedi.");
            }
            //çerezler giriş yap butonuna tıklanmasına engel oluyordu. çerezleri tıklayıp kapattık.

            sleep(3000);


                WebElement epostaAdresi = chromeDriver.findElement(By.id("login-email"));
                epostaAdresi.sendKeys("testdeneme2468@gmail.com");

                WebElement sifre = chromeDriver.findElement(By.id("login-password-input"));
                sifre.sendKeys("abcdef"); //yanlış şifre bilgisi girdik.

                WebElement girisYapButonu = chromeDriver.findElement(By.xpath("//button[@class='q-primary q-fluid q-button-medium q-button submit']/span[.='Giriş Yap']"));
                girisYapButonu.click();

                sleep(3000);

            if (chromeDriver.findElement(By.cssSelector(".message")).isDisplayed()) {
                System.out.println("E-posta adresiniz ve/veya şifreniz hatalı! ");
            }
            else{
                System.out.println("Başarılı giriş yaptınız!");
            }

            //HW-3.MADDE:Attempts are made to log in to the site with incorrect information.

            WebElement aramaKutusu = chromeDriver.findElement(By.xpath("//input[@class='V8wbcUhU']"));

            aramaKutusu.sendKeys("laptop");
            aramaKutusu.sendKeys(Keys.RETURN); // Enter tuşuna bas
            //HW-4.MADDE: The word "laptop" is entered in the search box.

            List<WebElement> urunler = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.p-card-chldrn-cntnr")));


            // Rastgele ürün seçimi
            randomUrunSecimi( urunler);
            //HW-5.MADDE: According to the result, a random product is selected from the products.


            sleep(3000);

            WebElement anladimButonu = chromeDriver.findElement(By.xpath("//button[text()='Anladım']"));

            anladimButonu.click();

            sleep(3000);


            String fiyat = chromeDriver.findElement(By.cssSelector(".product-price-container .prc-dsc")).getText();
            System.out.println(fiyat);

            WebElement sepeteEkle = chromeDriver.findElement(By.cssSelector(".add-to-basket-button-text"));
            sepeteEkle.click();
            //HW-6.MADDE : The selected product is added to the cart.

            sleep(3000);

            WebElement Sepetim = chromeDriver.findElement(By.xpath("//p[.='Sepetim']"));
            Sepetim.click();

            sleep(5000);

            anladimButonu = chromeDriver.findElement(By.xpath("//button[text()='Anladım']"));
            anladimButonu.click();

            String sepettekiFiyat = chromeDriver.findElement(By.cssSelector(".pb-basket-item-price")).getText();
            System.out.println(sepettekiFiyat);

            double dSepettekiFiyat = strToDouble(sepettekiFiyat);
            double dFiyat = strToDouble(fiyat);
            if (dSepettekiFiyat==dFiyat) {
                System.out.println
                        ("Ürünü sepete eklemeden önceki fiyatı: " + dFiyat + "'dir. Sepete ekledikten sonraki fiyatı: " + dSepettekiFiyat + "'dır. Fiyatlar aynıdır! ");

            }
            else{
                    System.out.println
                ("Ürünü sepete eklemeden önceki fiyatı: " + dFiyat + "'dir. Sepete ekledikten sonraki fiyatı: " + dSepettekiFiyat + "'dır. Fiyatlar farklıdır! ");
            }

            //HW-7.MADDE: Is the price of the product before adding to the cart the same as the price after adding it to the cart?

            WebElement artiButonu = chromeDriver.findElement(By.cssSelector("[aria-label='Ürün adedi arttırma']"));

            boolean isDisabled = artiButonu.getAttribute("disabled") != null; // artırma elementinin devre dışı olup olmadığını kontrol ediyoruz.
        if (isDisabled) { //true ise;
            System.out.println("Bu üründe ikinci ürün sepete eklenemiyor!");
        }else{// false ise: ürün arttırımı yapabiliyoruz.
            artiButonu.click();
        }

            sleep(2000);

            String sepettekiDoubleFiyat = chromeDriver.findElement(By.cssSelector(".pb-basket-item-price")).getText();
            System.out.println(sepettekiDoubleFiyat);
            double dSepettekiDoubleFiyat = strToDouble(sepettekiDoubleFiyat);

            if(isDisabled){
                System.out.println("ürün eklenemediği için çift ürün fiyat kontrolü yapılamamaktadır!");
            }
            else if(dSepettekiFiyat * 2 == dSepettekiDoubleFiyat)
            {
                System.out.println("iki ürün fiyatı doğru hesaplanmıştır!");
            }
            else{
                System.out.println("iki ürün fiyatı yanlış hesaplanmıştır!");
            }
//HW-8.MADDE: Increase the number of product in the basket to two and check its accuracy.

            WebElement silButonu = chromeDriver.findElement(By.xpath("//span[.='Sil']"));
            silButonu.click();

            sleep(3000);

            Sepetim = chromeDriver.findElement(By.xpath("//p[.='Sepetim']"));
            Sepetim.click();

            sleep(3000);

            if (elementVarMi("//span[.='Sepetinde ürün bulunmamaktadır.']")) {
                System.out.println("Sepet Boş");
            }else {
                System.out.println("Hata Var!");
            }
        }

        public void sleep(int time){
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        public void randomUrunSecimi(List<WebElement> urunler){

            Random random = new Random();
            int index = random.nextInt(urunler.size());
            WebElement randomUrun = urunler.get(index);
            randomUrun.click();
            WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));  // İkinci pencere açılmasını bekledik

            // Sekmelerin ID'lerini aldık
            List<String> windowHandles = new ArrayList<>(chromeDriver.getWindowHandles());

            // Yeni sekmeye geçiş yaptık
            chromeDriver.switchTo().window(windowHandles.get(1));  // İkinci sekmeye geçiş yaptık

        }

        public double strToDouble(String str ) {
            String sayisalString = str.replace(" TL", "").replace(".", "").replace(",", ".");


                // String'i double'a çevirme
                return Double.parseDouble(sayisalString);

        }

        public boolean elementVarMi( String xpathExp) {
            try {
                WebElement check = chromeDriver.findElement(By.xpath(xpathExp));
                return check.isDisplayed(); // Element varsa ve görünüyorsa true döner
            } catch (NoSuchElementException e) {
                return false; // Element yoksa false döner
            }
        }

    }

