/***************************** BEGIN LICENSE BLOCK ***************************
 The contents of this file are subject to the Mozilla Public License, v. 2.0.
 If a copy of the MPL was not distributed with this file, You can obtain one
 at http://mozilla.org/MPL/2.0/.

 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.

 Copyright (C) 2026 GeoRobotix Innovative Research, Inc. All Rights Reserved.
 ******************************* END LICENSE BLOCK ***************************/
package com.georobotix.ui.nocode;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import net.opengis.swe.v20.DataComponent;
import org.sensorhub.api.datastore.obs.IDataStreamStore;
import org.sensorhub.ui.GenericConfigForm;
import org.sensorhub.ui.data.MyBeanItem;

import java.util.*;

public class NoCodeEditorForm extends GenericConfigForm {
    private final Map<String, DataComponent> dataStreamMap = new LinkedHashMap<>();
    private final List<MappingEntry> mappingEntries = new ArrayList<>();
    //import org.vast.swe.fast.JsonDataWriterGson;
    //import org.vast.swe.fast.TextDataWriter;
    //import org.vast.swe.fast.XmlDataWriter;
    //TODO: dont hardcode these, use submodules similar to parsers when avail
    private static final List<String> serializerFormats = Arrays.asList("JSON", "CSV", "XML", "Protobuf", "CoT");

    @Override
    protected boolean isFieldVisible(String propId) {
        if (propId.endsWith("mappings"))
            return false;
        return super.isFieldVisible(propId);
    }

    @Override
    public void build(String title, String popupText, MyBeanItem<Object> beanItem, boolean includeSubForms) {
        super.build(title, popupText, beanItem, includeSubForms);

        if (beanItem.getBean() instanceof NoCodeEditor) {
            NoCodeEditor editor = (NoCodeEditor) beanItem.getBean();
            mappingEntries.clear();
            mappingEntries.addAll(editor.mappings);

            loadDataStreams();
            buildMappingLayoutUI(editor);
        }
    }

    private void loadDataStreams() {
        dataStreamMap.clear();
        getParentHub().getDatabaseRegistry().getObsSystemDatabases().forEach(database -> {
            IDataStreamStore dsStore = database.getDataStreamStore();
            dsStore.forEach((key, dsInfo) -> {
                String name = dsInfo.getName();
                if (name != null && !name.isEmpty()) {
                    dataStreamMap.put(name, dsInfo.getRecordStructure());
                }
            });
        });
    }

    private void buildMappingLayoutUI(NoCodeEditor editor) {
        VerticalLayout container = new VerticalLayout();
        container.setSpacing(true);
        container.setMargin(new MarginInfo(true, false));
        container.setWidth("100%");

        Label header = new Label("Mapping Configuration");
        header.addStyleName("h3");
        container.addComponent(header);

        VerticalLayout mappingsLayout = new VerticalLayout();
        mappingsLayout.setSpacing(true);
        mappingsLayout.setWidth("100%");

        Button addBtn = new Button("Add Mapping");
        addBtn.addClickListener(e -> {
            MappingEntry newEntry = new MappingEntry();
            editor.mappings.add(newEntry);
            mappingEntries.add(newEntry);
            mappingsLayout.addComponent(buildMappingRow(newEntry, mappingsLayout, editor));
        });
        container.addComponent(addBtn);

        for (MappingEntry entry : mappingEntries) {
            mappingsLayout.addComponent(buildMappingRow(entry, mappingsLayout, editor));
        }

        container.addComponent(mappingsLayout);

        addComponent(container);
    }


    private Panel buildMappingRow(MappingEntry entry, VerticalLayout mappingsLayout, NoCodeEditor editor) {
        Panel panel = new Panel();
        panel.setWidth("100%");

        VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSpacing(true);
        panelContent.setMargin(true);
        panelContent.setWidth("100%");

        HorizontalLayout topRow = new HorizontalLayout();
        topRow.setSpacing(true);
        topRow.setWidth("100%");

        ComboBox<String> datastreamCombo = new ComboBox<>("Datastream");
        datastreamCombo.setWidth("100%");
        datastreamCombo.setEmptySelectionAllowed(false);
        datastreamCombo.setItems(dataStreamMap.keySet());
        datastreamCombo.setPlaceholder("Select a datastream...");
        if (!entry.datastreamName.isEmpty()) {
            datastreamCombo.setValue(entry.datastreamName);
        }

        ComboBox<String> serializerCombo = new ComboBox<>("Serializer");
        serializerCombo.setWidth("200px");
        serializerCombo.setEmptySelectionAllowed(false);
        serializerCombo.setItems(serializerFormats);
        serializerCombo.setValue(entry.serializer);
        serializerCombo.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                entry.serializer = e.getValue();
            }
        });

        // if the serializer has templates (CoT ? / protobuf) then we should let the user select it
        // and it will auto generate the fields to map to
        Button removeBtn = new Button("Remove");
        removeBtn.addStyleName("danger");
        removeBtn.addClickListener(e -> {
            editor.mappings.remove(entry);
            mappingEntries.remove(entry);
            mappingsLayout.removeComponent(panel);
        });

        topRow.addComponents(datastreamCombo, serializerCombo, removeBtn);
        topRow.setExpandRatio(datastreamCombo, 1f);
        topRow.setComponentAlignment(removeBtn, Alignment.BOTTOM_RIGHT);
        panelContent.addComponent(topRow);

        VerticalLayout fieldRows = new VerticalLayout();
        fieldRows.setSpacing(true);
        fieldRows.setWidth("100%");
        fieldRows.setMargin(false);


        List<String> sourceFields = new ArrayList<>();
        List<ComboBox<String>> sourceCombos = new ArrayList<>();

        if (!entry.datastreamName.isEmpty() && dataStreamMap.containsKey(entry.datastreamName)) {
            DataComponent recordStructure = dataStreamMap.get(entry.datastreamName);
            if (recordStructure != null) {
                collectFieldPaths(recordStructure, "", sourceFields);
            }
        }

        HorizontalLayout headerRow = new HorizontalLayout();
        headerRow.setSpacing(true);
        headerRow.setWidth("100%");
        Label outputHeader = new Label("Output Field");
        outputHeader.setWidth("100%");
        outputHeader.addStyleName("bold");
        Label spacer = new Label("");
        spacer.setWidthUndefined();
        Label sourceHeader = new Label("Source Field");
        sourceHeader.setWidth("100%");
        sourceHeader.addStyleName("bold");
        headerRow.addComponents(outputHeader, spacer, sourceHeader);
        headerRow.setExpandRatio(outputHeader, 1f);
        headerRow.setExpandRatio(sourceHeader, 1f);
        fieldRows.addComponent(headerRow);

        for (Map.Entry<String, String> fieldEntry : entry.fieldMappings.entrySet()) {
            fieldRows.addComponent(buildFieldRow(fieldEntry.getKey(), fieldEntry.getValue(), entry, fieldRows, sourceFields, sourceCombos));
        }

        Button addFieldBtn = new Button("Add Field");
        addFieldBtn.addClickListener(e -> {
            String outputName = "field" + (entry.fieldMappings.size() + 1);
            entry.fieldMappings.put(outputName, "");
            int idx = fieldRows.getComponentIndex(addFieldBtn);
            fieldRows.addComponent(buildFieldRow(outputName, "", entry, fieldRows, sourceFields, sourceCombos), idx);
        });
        fieldRows.addComponent(addFieldBtn);

        datastreamCombo.addValueChangeListener(e -> {
            String dsName = e.getValue();
            if (dsName == null) return;
            entry.datastreamName = dsName;

            sourceFields.clear();
            DataComponent recordStructure = dataStreamMap.get(dsName);
            if (recordStructure != null) {
                collectFieldPaths(recordStructure, "", sourceFields);
            }

            for (ComboBox<String> combo : sourceCombos) {
                combo.clear();
                combo.setItems(sourceFields);
            }
            entry.fieldMappings.replaceAll((key, value) -> "");
        });

        panelContent.addComponent(fieldRows);
        panel.setContent(panelContent);

        return panel;
    }

    private HorizontalLayout buildFieldRow(String outputName, String sourceName, MappingEntry entry, VerticalLayout fieldRows, List<String> sourceFields, List<ComboBox<String>> sourceCombos) {
        HorizontalLayout row = new HorizontalLayout();
        row.setSpacing(true);
        row.setWidth("100%");

        TextField outputField = new TextField();
        outputField.setWidth("100%");
        outputField.setValue(outputName);
        outputField.setPlaceholder("Output field name");
        String[] currentKey = {outputName};
        outputField.addValueChangeListener(e -> {
            String sourceVal = entry.fieldMappings.remove(currentKey[0]);
            if (sourceVal == null) sourceVal = "";
            currentKey[0] = e.getValue();
            entry.fieldMappings.put(currentKey[0], sourceVal);
        });

        ComboBox<String> sourceCombo = new ComboBox<>();
        sourceCombo.setWidth("100%");
        sourceCombo.setEmptySelectionAllowed(true);
        sourceCombo.setItems(sourceFields);
        sourceCombo.setPlaceholder("Select source field...");
        if (sourceName != null && !sourceName.isEmpty() && sourceFields.contains(sourceName)) {
            sourceCombo.setValue(sourceName);
        }
        sourceCombos.add(sourceCombo);
        sourceCombo.addValueChangeListener(e -> {
            String currentOutputName = outputField.getValue();
            if (e.getValue() != null) {
                entry.fieldMappings.put(currentOutputName, e.getValue());
            } else {
                entry.fieldMappings.put(currentOutputName, "");
            }
        });

        Label colonLabel = new Label(":");
        colonLabel.setWidthUndefined();

        Button removeFieldBtn = new Button("\u00D7");
        removeFieldBtn.addStyleName("small");
        removeFieldBtn.addClickListener(e -> {
            entry.fieldMappings.remove(outputField.getValue());
            sourceCombos.remove(sourceCombo);
            fieldRows.removeComponent(row);
        });

        row.addComponents(outputField, colonLabel, sourceCombo, removeFieldBtn);
        row.setExpandRatio(outputField, 1f);
        row.setExpandRatio(sourceCombo, 1f);
        row.setComponentAlignment(removeFieldBtn, Alignment.MIDDLE_CENTER);

        return row;
    }

    private void collectFieldPaths(DataComponent component, String prefix, List<String> paths) {
        for (int i = 0; i < component.getComponentCount(); i++) {
            DataComponent child = component.getComponent(i);
            String path = prefix.isEmpty() ? child.getName() : prefix + "/" + child.getName();

            // need to check nested fields
            if (child.getComponentCount() > 0)
                collectFieldPaths(child, path, paths);
            else
                paths.add(path);
        }
    }
}
