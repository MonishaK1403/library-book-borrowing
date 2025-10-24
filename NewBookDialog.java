import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class NewBookDialog extends JDialog {
    private JTextField idField = new JTextField(20);
    private JTextField titleField = new JTextField(20);
    private JTextField authorField = new JTextField(20);

    private boolean saved = false;

    public NewBookDialog(JFrame owner) {
        super(owner, "Add New Book", true);
        init();
    }

    private void init() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("ID:"), c);
        c.gridx = 1; form.add(idField, c);

        c.gridx = 0; c.gridy = 1; form.add(new JLabel("Title:"), c);
        c.gridx = 1; form.add(titleField, c);

        c.gridx = 0; c.gridy = 2; form.add(new JLabel("Author:"), c);
        c.gridx = 1; form.add(authorField, c);

        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener((ActionEvent e) -> onSave());
        cancelBtn.addActionListener((ActionEvent e) -> onCancel());

        JPanel buttons = new JPanel();
        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(form, BorderLayout.CENTER);
        this.getContentPane().add(buttons, BorderLayout.SOUTH);

        this.pack();
        this.setLocationRelativeTo(getOwner());
    }

    private void onSave() {
        if (idField.getText().trim().isEmpty() || titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID and Title are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        saved = true;
        setVisible(false);
    }

    private void onCancel() {
        saved = false;
        setVisible(false);
    }

    public boolean isSaved() { return saved; }

    public String getBookId() { return idField.getText().trim(); }
    public String getBookTitle() { return titleField.getText().trim(); }
    public String getBookAuthor() { return authorField.getText().trim(); }

    public void setDefaults(String id, String title, String author) {
        idField.setText(id);
        titleField.setText(title);
        authorField.setText(author);
    }
}

