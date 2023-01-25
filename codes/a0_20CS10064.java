//  Name:       Subhajyoti Halder
//  Roll no:    20CS10064

import java.util.*;

public class a0_20CS10064{
    //  List of all entities
    static List<manufacturer> lm=new ArrayList<manufacturer>();
    static List<customer> lc=new ArrayList<customer>();
    static List<product> lp=new ArrayList<product>();
    static List<shop> ls=new ArrayList<shop>();
    static List<delivery> ld=new ArrayList<delivery>();

    //  Search for a manufacturer, using given id
    static manufacturer findManufacturer(int m_id){
        for(manufacturer M : lm){
            if(M.id == m_id)
                return M;
        }
        return (new manufacturer(0,"not_found"));
    }

    //  Search for a customer, using given id
    static customer findCustomer(int i){
        for(customer C : lc){
            if(C.id == i)
                return C;
        }
        return (new customer(0,"not_found",0));
    }

    //  Search for a product, using given id
    static product findProduct(int i){
        for(product P : lp){
            if(P.id == i)
                return P;
        }
        return (new product(0,"not_found",0));
    }
    //  Create a new product from user input, and return it
    static product newProduct(int m_id){
        System.out.print("Enter id: ");
        Scanner sc=new Scanner(System.in);
        int id=sc.nextInt();

        System.out.print("Enter name: ");
        Scanner sc2=new Scanner(System.in);
        String name=sc2.nextLine();

        if(m_id==0){            //  if Manufacturer-id is to be taken as input
            System.out.print("Enter manufacturer id: ");
            Scanner sc3=new Scanner(System.in);
            int k=sc3.nextInt();

            P_to_M(id, k);      //  update new product to Manufacturer's product list
            return new product(id,name,k);
        }
        else{
            P_to_M(id, m_id);   //  update new product to Manufacturer's product list
            return new product(id,name,m_id);
        }
    }
    //  Updates manufacturer's product list with new product
    static void P_to_M(int pid,int mid){
        manufacturer M=findManufacturer(mid);
        M.p_id.add(pid);
    }

    //  Search for a shop, using given id
    static shop findShop(int i){
        for(shop S : ls)
            if(S.id == i)
                return S;
        return (new shop(0,"not_found",0));
    }

    static void viewPairList(List<Pair<Integer,Integer>> L){
        for(Pair<Integer,Integer> x: L){
            product p=findProduct(x.getL());
            System.out.println(p.name + " ("+p.id+")  -> " + x.getR());
        }
    }

    //  Find retailer to Place order
    static shop findShop_to_order(int z, int pid){
        for(shop s: ls)         //  Traverse accross all available shops
            if(s.zipcode == z){     //  look for zipcode match
                for(Pair<Integer,Integer> i: s.I)
                    if(i.getL() == pid)
                        return s;   //  return the compatible shop
                
            }
        return new shop(0,"not_found",0);
    }

    //  Find agent to deliver
    static delivery findAgent(int z){
        int found=0;
        delivery D=new delivery(0,"not_found",0);
        for(delivery d: ld)
            if(d.zipcode == z){
                if(found==0){
                    D=d;
                    found=1;
                }
                else if(D.n > d.n)      //  assign delivery agent with lesser deliveries
                    D=d;
            }
        return D;
    }

    //  Place an order on customer demand
    static void placeOrder(int pid,int c_id){
        customer C=findCustomer(c_id);
        shop S=findShop_to_order(C.zipcode, pid);       //  get compatible Retailer
        if(S.name == "not_found"){
            System.out.println("Seller Unavailable");
            return;
        }
        System.out.println("Connected to Seller : "+S.name);

        delivery D=findAgent(C.zipcode);                //  get available Delivery Agent
        if(D.name == "not_found"){
            System.out.println("Delivery Agent Unavailable");
            return;
        }
        System.out.println("Connected to Delivery Agent : "+D.name);
        System.out.println("Out for Delivery");

        for(Pair<Integer,Integer> x: S.I)               //  update Retailer Inventory
            if(x.getL() == pid){
                x.setR(x.getR()-1);
                if(x.getR()==0)
                    S.I.remove(x);
                break;
            }
        
        int found=0;
        for(Pair<Integer,Integer> x: C.purchased)
            if(x.getL() == pid){
                found=1;
                x.setR(x.getR()+1);
                break;
            }
        if(found==0){
            Pair<Integer,Integer> T=new Pair<Integer,Integer>(pid,1);
            C.purchased.add(T);
        }
        
        D.n = D.n +1;           //  update Delivery Agent delivery count
    }

    static void modifyManufacturer(int opr){
        if(opr==1){                         //  Add new Manufacturer Entity
            System.out.print("Enter id: ");
            Scanner sc1=new Scanner(System.in);
            int id=sc1.nextInt();

            System.out.print("Enter name: ");
            Scanner sc2=new Scanner(System.in);
            String name=sc2.nextLine();
            lm.add(new manufacturer(id,name));
        }
        else if(opr==2 || opr==3){          //  Display all Available Manufacturers
            System.out.println("List of all Manufacturers:");
            lm.forEach(x->{
                System.out.println(x.name + " ("+(x.id)+")");
            });
            if(opr==2){                     //  Delete Desired Manufacturer
                if(!lm.isEmpty()){
                    System.out.print("Enter Manufacturer id to remove: ");
                    Scanner sc=new Scanner(System.in);
                    int t=sc.nextInt();
                    lm.remove(findManufacturer(t));
                }
                else
                    System.out.println("No Manufacturer available");
            }
        }
    }

    static void modifyCustomer(int opr){
        if(opr==1){                         //  Add new Customer Entity
            System.out.print("Enter id: ");
            Scanner sc1=new Scanner(System.in);
            int id=sc1.nextInt();

            System.out.print("Enter name: ");
            Scanner sc2=new Scanner(System.in);
            String name=sc2.nextLine();

            System.out.print("Enter Zipcode: ");
            Scanner sc3=new Scanner(System.in);
            int z=sc3.nextInt();
            lc.add(new customer(id,name,z));
        }
        else if(opr==2 || opr==3){          //  Display all Available Customers
            System.out.println("List of all Customers:");
            lc.forEach(x->{
                System.out.println(x.name + "("+(x.id)+")");
            });
            if(opr==2){                     //  Delete Desired Customer
                if(!lc.isEmpty()){
                    System.out.print("Enter Customer id to remove: ");
                    Scanner sc=new Scanner(System.in);
                    int t=sc.nextInt();
                    lc.remove(findCustomer(t));
                }
                else
                    System.out.println("No Customer Available");
            }
        }
    }

    static void modifyProduct(int opr){
        if(opr==1)                         //  Add new Product Entity
            lp.add(newProduct(0));
        else if(opr==2 || opr==3){          //  Display all Available Products
            System.out.println("List of all Products:");
            lp.forEach(x->{
                System.out.println(x.name + "("+(x.id)+")");
            });
            if(opr==2){                     //  Delete Desired Product
                if(!lp.isEmpty()){
                    System.out.print("Enter Product id to remove: ");
                    Scanner sc=new Scanner(System.in);
                    int t=sc.nextInt();
                    lp.remove(findProduct(t));
                }
                else
                    System.out.println("No Product Available");
            }
        }
    }

    static void modifyShop(int opr){
        if(opr==1){                         //  Add new Shop Entity
            System.out.print("Enter id: ");
            Scanner sc=new Scanner(System.in);
            int id=sc.nextInt();

            System.out.print("Enter name: ");
            Scanner sc2=new Scanner(System.in);
            String name=sc2.nextLine();

            System.out.print("Enter Zipcode: ");
            Scanner sc3=new Scanner(System.in);
            int z=sc3.nextInt();
            ls.add(new shop(id,name,z));
        }
        else if(opr==2 || opr==3){          //  Display all Available Shops
            System.out.println("List of all Shops & Warehouses:");
            ls.forEach(x->{
                System.out.println(x.name + "("+(x.id)+")");
            });
            if(opr==2){                     //  Delete Desired Shop
                if(!ls.isEmpty()){
                    System.out.print("Enter Shop & Warehouse id to remove: ");
                    Scanner sc=new Scanner(System.in);
                    int t=sc.nextInt();
                    ls.remove(findShop(t));
                }
                else
                    System.out.println("No Shop Available");
            }
        }
    }

    static void modifyDelivery(int opr){
        if(opr==1){                         //  Add new Delivery Agent Entity
            System.out.print("Enter id: ");
            Scanner sc=new Scanner(System.in);
            int id=sc.nextInt();

            System.out.print("Enter name: ");
            Scanner sc2=new Scanner(System.in);
            String name=sc2.nextLine();

            System.out.print("Enter Zipcode: ");
            Scanner sc3=new Scanner(System.in);
            int z=sc3.nextInt();
            ld.add(new delivery(id,name,z));
        }
        else if(opr==2 || opr==3){          //  Display all Available Delivery Agents
            System.out.println("List of all Delivery Agents:");
            ld.forEach(x->{
                System.out.println(x.name + "("+(x.id)+")");
            });
            if(opr==2){                     //  Delete Desired Delivery Agent
                if(!ld.isEmpty()){
                    System.out.print("Enter Delivery Agent id to remove: ");
                    Scanner sc=new Scanner(System.in);
                    int t=sc.nextInt();
                    for(delivery y : ld){
                        if(y.id == t){
                            ld.remove(y);
                            break;
                        }
                    }
                }
                else
                    System.out.println("Unavailable");
            }
        }
    }

    static void modifyEntity(){
        System.out.print("1. Manufacturer   2. Customer   3. Product   ");
        System.out.print("4. Shops and Warehouses   5. Delivery Agent\n");
        System.out.print("Choose entity to modify: ");

        Scanner scan = new Scanner(System.in);
        int entity = scan.nextInt();

        System.out.print("1. Create   2. Delete   3. Print List\n");
        System.out.print("Choose entity operation: ");
        int opr= scan.nextInt();

        if(entity==1)
            modifyManufacturer(opr);                
        else if(entity==2)
            modifyCustomer(opr);
        else if(entity==3)
            modifyProduct(opr);
        else if(entity==4)
            modifyShop(opr);
        else if(entity==5)
            modifyDelivery(opr);
    }

    static void loginManufacturer(int m_id){
        manufacturer M=findManufacturer(m_id);
        if(M.name == "not_found"){
            System.out.println("incorrect id");
            return;
        }

        System.out.println("\n1. Add product    2. List all products      0. Log-Out");
        System.out.print("Enter task: ");
        Scanner sc=new Scanner(System.in);
        int task=sc.nextInt();

        if(task==1)                     //  Add new product to Manufacturing
            lp.add(newProduct(m_id));
        else if(task==2){               //  Print list of all Manufactured Products
            System.out.println("List of products manufactured: ");
            for(int pid: M.p_id){
                product p=findProduct(pid);
                System.out.println(pid + ": "+p.name);
            }
        }
        
        if(task==0)
            return;
        else
            loginManufacturer(m_id);
        
        return;
    }

    static void loginCustomer(int c_id){
        customer C=findCustomer(c_id);
        if(C.name == "not_found"){
            System.out.println("incorrect id");
            return;
        }

        System.out.println("\n1. Place an Order    2. Purchase List      0. Log-Out");
        System.out.print("Enter task: ");
        Scanner sc=new Scanner(System.in);
        int task=sc.nextInt();

        if(task==1){                //  Place an Order
            System.out.print("Product id: ");
            Scanner sc2=new Scanner(System.in);
            int pid=sc2.nextInt();

            placeOrder(pid, c_id);
        }
        else if(task==2){           //  List of all Purchased Products
            System.out.println("Purchased products: ");
            viewPairList(C.purchased);
        }
        
        if(task==0)
            return;
        else
            loginCustomer(c_id);

        return;
    }

    static void loginShop(int s_id){
        shop S=findShop(s_id);
        if(S.name == "not_found"){
            System.out.println("incorrect id");
            return;
        }

        System.out.println("\n1. Add product    2. View Inventory      0. Log-Out");
        System.out.print("Enter task: ");
        Scanner sc=new Scanner(System.in);
        int task=sc.nextInt();

        if(task==1){                //  Add 'q' quantity of a product to Inventory
            System.out.print("Product id: ");
            Scanner sc2=new Scanner(System.in);
            int pid=sc2.nextInt();

            product P=findProduct(pid);
            if(P.name == "not_found"){
                System.out.println("incorrect id");
                loginShop(s_id);
                return;
            }

            System.out.print("Enter Quantity: ");
            Scanner sc3=new Scanner(System.in);
            int q=sc3.nextInt();

            int found=0;
            for(Pair<Integer,Integer> x: S.I)
                if(x.getL() == pid){
                    found=1;
                    x.setR(x.getR()+q);
                    break;
                }
            if(found==0){
                Pair<Integer,Integer> T=new Pair<Integer,Integer>(pid,q);
                S.I.add(T);
            }
        }
        else if(task==2){           //  Print a list of Products in Inventory
            System.out.println("Shop and Warehouse Inventory: ");
            viewPairList(S.I);
        }
        
        if(task==0)
            return;

        loginShop(s_id);
        return;
    }

    static void logIn(){
        System.out.print("1. Manufacturer    2. Customer    3. Retailer\n");
        System.out.print("Enter log-in type: ");
        Scanner sc=new Scanner(System.in);
        int role=sc.nextInt();

        System.out.print("Enter log-in id: ");
        Scanner sc2=new Scanner(System.in);
        int id=sc2.nextInt();
        
        if(role==1){
            System.out.println("Name: "+findManufacturer(id).name);
            loginManufacturer(id);
        }
        else if(role==2){
            System.out.println("Name: "+findCustomer(id).name);
            loginCustomer(id);
        }
        else if(role==3){
            System.out.println("Name: "+findShop(id).name);
            loginShop(id);
        }
        else
            logIn();
    }

    public static void main(String[] args){
        int choice =69;  
        while(true){
            System.out.println("\n1. Register     2. Log-in     0. Exit");
            System.out.print("Choice: ");
            Scanner scan = new Scanner(System.in);
            choice = scan.nextInt();

            if(choice==1)
                modifyEntity();
            else if(choice==2)
                logIn();
            else if(choice==0)
                break;
        }
    }
}
//  Below are the classes of Each Entities
class product{
    int id;     String name;    int m;
    public product(int i,String n,int m0){
        id=i;   name=n;     m=m0;
    }
}

class Pair<L,R>{
    private L l;
    private R r;
    public Pair(L l,R r){
        this.l = l;
        this.r = r;
    }

    public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
}

class customer{
    int id;     String name;
    int zipcode;
    List<Pair<Integer,Integer>> purchased;
    public customer(int i, String n,int z){
        id=i;   name=n;     zipcode=z;
        purchased =  new ArrayList<Pair<Integer,Integer>>();
    }
}

class shop{
    int id;     String name;
    int zipcode;
    List<Pair<Integer,Integer>> I;
    public shop(int i, String n,int z){
        id=i;   name=n;     zipcode=z;
        I= new ArrayList<Pair<Integer,Integer>>();
    }
}

class delivery{
    int id;     String name;
    int zipcode, n;
    public delivery(int i, String na,int z){
        id=i;   name=na;     zipcode = z;       n=0;
    }
}

class manufacturer{
    int id;     String name;
    List<Integer> p_id;
    public manufacturer(int i, String n){
        id=i;   name=n;     p_id=new ArrayList<Integer>();
    }
}