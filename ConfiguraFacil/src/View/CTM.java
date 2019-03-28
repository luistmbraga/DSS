package View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Grupo 10
 */
public class CTM extends AbstractTableModel{

    private List<Map.Entry<String,Double>> valores;
    private String coluna0;
    
    public CTM(Map<String,Double> v){
        super();
        this.coluna0 = "Componente";
        this.valores = new ArrayList<>(v.entrySet());
    }
    
    public CTM(Map<String,Double> v, String col){
        super();
        this.valores = new ArrayList<>(v.entrySet());
        this.coluna0 = col;
    }
    
    @Override
    public int getRowCount() {
        return valores.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) return valores.get(rowIndex).getKey();
        else return valores.get(rowIndex).getValue();
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) return coluna0;
        else return "Preço";
    }
    
}
