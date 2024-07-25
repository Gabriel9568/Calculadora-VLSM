package Controller;

import View.Fvlsm;
import Model.VLSMCalculator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VLSMController {
    private Fvlsm view;
    private VLSMCalculator model;

    public VLSMController(Fvlsm view, VLSMCalculator model) {
        this.view = view;
        this.model = model;

        view.setCalculateButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ipAddress = view.getIpAddress();
                int numberOfHosts = view.getNumberOfHosts();

                if (numberOfHosts >= 0) {
                    VLSMCalculator.SubnetInfo subnetInfo = model.calculateSubnet(ipAddress, numberOfHosts);
                    if (subnetInfo != null) {
                        view.addSubnetInfoToTable(subnetInfo);
                    } else {
                        JOptionPane.showMessageDialog(view, "Error al calcular la subred", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "Número de hosts inválido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        Fvlsm view = new Fvlsm();
        VLSMCalculator model = new VLSMCalculator();
        new VLSMController(view, model);
        view.setVisible(true);
    }
}
