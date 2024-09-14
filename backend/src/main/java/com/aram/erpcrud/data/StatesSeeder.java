package com.aram.erpcrud.data;

import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.locations.domain.StateRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatesSeeder {

    private final StateRepository stateRepository;

    public StatesSeeder(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public void seed() {
        stateRepository.saveAll(allStates());
    }

    private Iterable<State> allStates() {
        return List.of(
            new State("AGU", "Aguascalientes"),
            new State("BCN", "Baja California"),
            new State("BCS", "Baja California Sur"),
            new State("CAM", "Campeche"),
            new State("COA", "Coahuila"),
            new State("COL", "Colima"),
            new State("CHI", "Chiapas"),
            new State("CHI", "Chihuahua"),
            new State("CDMX", "Ciudad de México"),
            new State("DUR", "Durango"),
            new State("GUA", "Guanajuato"),
            new State("GUE", "Guerrero"),
            new State("HID", "Hidalgo"),
            new State("JAL", "Jalisco"),
            new State("MEX", "Estado de México"),
            new State("MIC", "Michoacán"),
            new State("MOR", "Morelos"),
            new State("NAY", "Nayarit"),
            new State("NLE", "Nuevo León"),
            new State("OAX", "Oaxaca"),
            new State("PUE", "Puebla"),
            new State("QUE", "Querétaro"),
            new State("ROO", "Quintana Roo"),
            new State("SLP", "San Luis Potosí"),
            new State("SIN", "Sinaloa"),
            new State("SON", "Sonora"),
            new State("TAB", "Tabasco"),
            new State("TAM", "Tamaulipas"),
            new State("TLA", "Tlaxcala"),
            new State("VER", "Veracruz"),
            new State("YUC", "Yucatán"),
            new State("ZAC", "Zacatecas")
        );
    }
}