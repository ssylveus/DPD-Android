# DPD-Android
Deployd is a tool that makes building APIs simple by providing important ready-made functionality out of the box that meet the demands of complex applications (http://deployd.com).
DPD-Android is an android library, that helps facilitate the use of Deployd for android Development.

#Features
- DPDObject
- DPDUser (Login, Logout)
- DPDQuery
- DPDRequest
- DPDCLient

#The Basics
- DPDSwift Uses Jackson Library for object mapping. More information can be found here.  
- https://github.com/FasterXML/jackson-core

- Assuming we have a collection on Deployd called Stores.  We can access the store collection as follow.

- Using DPDObject

```java
public class Store extends DPDObject {
    private String name;
    private String city;
    private String state;
    private String zip;

    public Store() {

    }

    public Store(String name, String city, String state, String zip) {
        this.name = name;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}

============================ Inside Activity ============================

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createStore();
        //loadStroe();
    }

    void createStore() {
        Store store = new Store("Best Buy", "Orlando", "Florida", "32818");
        store.createObject("store", Store.class, new MappableResponseCallBack() {
            @Override
            public void onResponse(List<DPDObject> response) {
                if (response != null) {
                    Store store = (Store) response.get(0);
                }
            }

            @Override
            public void onFailure(Call call, Response response, Exception e) {
                Log.d(this.getClass().getSimpleName(), "Failed to create store");
            }

        });
    }

    void loadStroe() {
        DPDQuery query = new DPDQuery(QueryCondition.EQUAL, null, null, null, "name", "Best Buy", null);
        query.findMappableObject("stores", Store.class, new MappableResponseCallBack() {
            @Override
            public void onResponse(List<DPDObject> response) {
                Store store = (Store) response.get(0);
            }

            @Override
            public void onFailure(Call call, Response response, Exception e) {
                Log.d(this.getClass().getSimpleName(), "Failed to create store");
            }
        });
    }
}

```
