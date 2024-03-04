using Lab11.Service;

namespace Lab11.Ui;

public class Ui(DocumentService documentService, FacturaService facturaService, AchizitieService achizitieService)
{
    public void RunUi()
    {
        PrintMenu();        
        while (true)
        {
            var option = ReadOption();
            switch (option)
            {
                case 1:
                    PrintAllDocuments();
                    break;
                case 2:
                    PrintAllBills();
                    break;
                case 3:
                    PrintAllPurchases();
                    break;
                case 4:
                    RunTask1();
                    break;
                case 5:
                    RunTask2();
                    break;
                case 6:
                    RunTask3();
                    break;
                case 7:
                    RunTask4();
                    break;
                case 8:
                    RunTask5();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void RunTask1()
    {
        documentService.GetDocumentInAn(2023).ForEach(Console.WriteLine);
    }
    
    private void RunTask2()
    {
        facturaService.GetFacturiLunaCurenta().ForEach(Console.WriteLine);
    }
    
    private void RunTask3()
    {
        facturaService.GetFacturiCuMacar3Prod().ForEach(Console.WriteLine);
    }

    
    private void RunTask5()
    {
        Console.WriteLine(facturaService.GetCategorieCuCmmBaniCheltuiti());
    }

    private void RunTask4()
    {
        achizitieService.GetAllAchizitiiInCategorie("Utilities").ForEach(Console.WriteLine);
    }
    
    private void PrintAllPurchases()
    {
        achizitieService.GetAllPurchases().ForEach(Console.WriteLine);
    }

    private void PrintAllBills()
    {
        facturaService.GetAllFacturi().ForEach(Console.WriteLine);
    }

    private void PrintAllDocuments()
    {
        documentService.GetAllDocuments().ForEach(Console.WriteLine);
    }

    private static int ReadOption()
    {
        Console.WriteLine("Enter option: ");
        return Convert.ToInt32(Console.ReadLine());
    }

    private static void PrintMenu()
    {
        Console.WriteLine("0. Exit");
        Console.WriteLine("1. Afiseaza documentele");
        Console.WriteLine("2. Afiseaza facturile");
        Console.WriteLine("3. Afiseaza achizitiile");
        Console.WriteLine("4. Afiseaza toate documentele emise in 2023");
        Console.WriteLine("5. Afiseaza toate facturile scadente in luna curenta");
        Console.WriteLine("6. Afiseaza toate facturile cu cel putin 3 produse achizitionate");
        Console.WriteLine("7. Afiseaza toate achizitiile din categoria Utilities");
        Console.WriteLine("8. Afiseaza categoria de facturi care a inregistrat cele mai multe cheltuieli");
    }
}