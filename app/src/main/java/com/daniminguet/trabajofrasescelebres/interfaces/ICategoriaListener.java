package com.daniminguet.trabajofrasescelebres.interfaces;

import java.io.Serializable;

public interface ICategoriaListener extends Serializable {
    void onCategoriaSeleccionada(int id);
}
