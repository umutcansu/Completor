

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompletorSettingsComponent {
    public JPanel mainPanel;
    private JPanel filePathPanel;
    private JPanel extensionsPanel;
    private JTextField otherExtensionsField;
    private JTextField dataPathQueryField;
    private JTextField dataValuePathQueryField;
    private JCheckBox insertValueCheckbox;

    private TextFieldWithBrowseButton dataFilePathField;
    private final Map<String, JCheckBox> extensionCheckboxes = new LinkedHashMap<>();

    public CompletorSettingsComponent() {
        setupUI();
    }

    private void setupUI() {
        dataFilePathField = new TextFieldWithBrowseButton();
        filePathPanel.setLayout(new BorderLayout());
        filePathPanel.add(dataFilePathField, BorderLayout.CENTER);

        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        descriptor.setTitle("Select Data File");
        descriptor.setDescription("Select the JSON or XML file containing completion data.");
        dataFilePathField.addBrowseFolderListener("Select Data File", null, null, descriptor);

        Map<String, String> commonExtensions = new LinkedHashMap<>();
        commonExtensions.put("kt", "Kotlin");
        commonExtensions.put("java", "Java");
        commonExtensions.put("xml", "XML");
        commonExtensions.put("json", "JSON");

        for (Map.Entry<String, String> entry : commonExtensions.entrySet()) {
            String extension = entry.getKey();
            String displayName = entry.getValue();
            JCheckBox checkBox = new JCheckBox(displayName);
            extensionsPanel.add(checkBox);
            extensionCheckboxes.put(extension, checkBox);
        }
    }

    public JPanel getPanel() { return mainPanel; }
    public TextFieldWithBrowseButton getDataFilePathField() { return dataFilePathField; }
    public JTextField getOtherExtensionsField() { return otherExtensionsField; }
    public JTextField getDataPathQueryField() { return dataPathQueryField; }
    public JTextField getDataValuePathQueryField() { return dataValuePathQueryField; }
    public JCheckBox getInsertValueCheckbox() { return insertValueCheckbox; }
    public Map<String, JCheckBox> getExtensionCheckboxes() { return extensionCheckboxes; }
}