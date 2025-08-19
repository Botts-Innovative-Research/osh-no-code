import com.botts.api.parser.data.BaseDataType;
import com.botts.api.parser.data.DataField;
import com.botts.api.parser.data.FieldMapping;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTest {

    List<DataField> sampleFields;
    List<FieldMapping> sampleMapping;

    @Before
    public void setup() {
        sampleFields = new ArrayList<>();
        sampleMapping = new ArrayList<>();
        DataField field = new DataField();
        field.dataType = BaseDataType.FLOAT;
        field.name = "test";
        field.index = 0;
        sampleFields.add(field);

        FieldMapping mapping = new FieldMapping();
        mapping.inputFieldName = "test";
        mapping.outputFieldName = "test";
        sampleMapping.add(mapping);
    }

    @Test
    public void testXML(){
    }
}
