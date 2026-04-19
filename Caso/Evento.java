package Caso;

public class Evento {
    private final long id;
    private final int sensorId;
    private final int categoria;
    private final int destinoServidor;
    private final TipoEvento tipo;

    private Evento(long id, int sensorId, int categoria, int destinoServidor, TipoEvento tipo) {
        this.id = id;
        this.sensorId = sensorId;
        this.categoria = categoria;
        this.destinoServidor = destinoServidor;
        this.tipo = tipo;
    }

    public static Evento normal(long id, int sensorId, int categoria, int destinoServidor) {
        return new Evento(id, sensorId, categoria, destinoServidor, TipoEvento.NORMAL);
    }

    public static Evento alerta(long id, int sensorId, int categoria, int destinoServidor) {
        return new Evento(id, sensorId, categoria, destinoServidor, TipoEvento.ALERTA);
    }

    public static Evento fin() {
        return new Evento(-1L, -1, -1, -1, TipoEvento.FIN);
    }

    public Evento comoNormal() {
        return new Evento(id, sensorId, categoria, destinoServidor, TipoEvento.NORMAL);
    }

    public long getId() {
        return id;
    }

    public int getSensorId() {
        return sensorId;
    }

    public int getCategoria() {
        return categoria;
    }

    public int getDestinoServidor() {
        return destinoServidor;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public boolean esFin() {
        return tipo == TipoEvento.FIN;
    }

    @Override
    public String toString() {
        if (esFin()) {
            return "Evento{FIN}";
        }
        return "Evento{id=" + id
            + ", sensor=" + sensorId
            + ", categoria=" + categoria
            + ", destino=" + destinoServidor
            + ", tipo=" + tipo + "}";
    }
}
