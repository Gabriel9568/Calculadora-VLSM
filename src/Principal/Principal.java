
package Principal;

import Controller.VLSMController;
import Model.VLSMCalculator;
import View.Fvlsm;

public class Principal {

    public static void main(String[] args) {
        // 
        Fvlsm view = new Fvlsm();
        VLSMCalculator model = new VLSMCalculator();
        new VLSMController(view, model);
        view.setVisible(true);
    }
    
}
