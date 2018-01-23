package mvc.model;

public class Kontakt {

    private Integer id;
    private String imie;
    private String nazwisko;
    private String adres;
    private String kraj;

    private Integer numer_id;
    private Integer kontakt_id;
    private Integer numer;
    private String operator;

    public Kontakt(Integer id, String imie, String nazwisko, String adres, String kraj, Integer numer, String operator) {
        this(imie, nazwisko, adres, kraj, numer, operator);
        this.id = id;
    }

    public Kontakt(String imie, String nazwisko, String adres, String kraj, Integer numer, String operator) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.adres = adres;
        this.kraj = kraj;
        this.numer = numer;
        this.operator = operator;
    }

    public Kontakt(Integer id, String imie, String nazwisko, String adres, String kraj) {
        this(imie, nazwisko, adres, kraj);
        this.id = id;
    }

    public Kontakt(String imie, String nazwisko, String adres, String kraj) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.adres = adres;
        this.kraj = kraj;
    }

    public Kontakt(Integer numer_id, Integer kontakt_id, Integer numer, String operator) {
        this(kontakt_id, numer, operator);
        this.numer_id = numer_id;
    }

    public Kontakt(Integer kontakt_id, Integer numer, String operator) {
        this.kontakt_id = kontakt_id;
        this.numer = numer;
        this.operator = operator;
    }


    public Integer getId() {
        return id;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getAdres() {
        return adres;
    }

    public String getKraj() {
        return kraj;
    }


    public Integer getNumer_id() {
        return numer_id;
    }

    public Integer getKontakt_id() {
        return kontakt_id;
    }

    public Integer getNumer() {
        return numer;
    }

    public String getOperator() {
        return operator;
    }

}
