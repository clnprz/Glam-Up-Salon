import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NewClass extends javax.swing.JFrame {

    private List<String[]> users        = new ArrayList<>();
    private List<String[]> customers    = new ArrayList<>();
    private List<String[]> stylists     = new ArrayList<>();
    private List<String[]> services     = new ArrayList<>();
    private List<String[]> appointments = new ArrayList<>();
    private List<String[]> payments     = new ArrayList<>();

    private int custSeq=3,stylSeq=4,svcSeq=7,apptSeq=3,paySeq=2;
    private String[] currentUser = null;

    // ── Layout constants ──────────────────────────────────────────
    private static final int WIN_W   = 1110;
    private static final int WIN_H   = 620;
    private static final int HDR_H   = 55;   // header bar height
    private static final int NAV_W   = 160;  // left nav width
    private static final int CONTENT_X = NAV_W;          // left edge of content area
    private static final int CONTENT_Y = HDR_H + 10;     // top of content area (65)
    private static final int CONTENT_W = WIN_W - NAV_W;  // 950
    private static final int CONTENT_H = WIN_H - HDR_H - 10 - 30; // 525  (leaves 30px footer)

    // For pages with a form panel on the left + table on the right
    private static final int FORM_W   = 360;
    private static final int FORM_H   = CONTENT_H;       // 525 – fills content height
    private static final int TABLE_X  = CONTENT_X + FORM_W + 10;
    private static final int TABLE_W  = WIN_W - TABLE_X - 10;
    private static final int TABLE_H  = CONTENT_H;

    public NewClass() {
        seedData();
        initComponents();
        CardLayout cl=(CardLayout)getContentPane().getLayout();
        cl.show(getContentPane(),"login");
    }

    private void seedData() {
        users.add(new String[]{"U001","Admin User","admin@glamup.com","admin123","Admin"});
        users.add(new String[]{"U002","Staff One","staff@glamup.com","staff123","Staff"});
        users.add(new String[]{"U003","Jane Customer","jane@email.com","jane123","Customer"});
        stylists.add(new String[]{"S1","Liza Santos","Coloring & Highlights","Active"});
        stylists.add(new String[]{"S2","Marco Reyes","Rebonding & Treatments","Active"});
        stylists.add(new String[]{"S3","Ana Cruz","Manicure & Pedicure","Active"});
        services.add(new String[]{"SV1","Haircut","250","45"});
        services.add(new String[]{"SV2","Hair Coloring","1200","120"});
        services.add(new String[]{"SV3","Rebonding","2500","180"});
        services.add(new String[]{"SV4","Hair Treatment","800","60"});
        services.add(new String[]{"SV5","Manicure","250","45"});
        services.add(new String[]{"SV6","Pedicure","350","60"});
        customers.add(new String[]{"C1","Jane Customer","09171234567","jane@email.com"});
        customers.add(new String[]{"C2","Maria Dela Cruz","09181234567","maria@email.com"});
        appointments.add(new String[]{"A1","C1","S1","2025-07-15","10:00","Scheduled","SV1,SV2","1450","U002"});
        appointments.add(new String[]{"A2","C2","S3","2025-07-16","14:00","Completed","SV5,SV6","600","U002"});
        payments.add(new String[]{"P1","A2","600","Cash","Paid","2025-07-16"});
    }

    private String customerName(String id){for(String[]c:customers)if(c[0].equals(id))return c[1];return id;}
    private String stylistName(String id){for(String[]s:stylists)if(s[0].equals(id))return s[1];return id;}
    private String serviceNames(String ids){
        StringBuilder sb=new StringBuilder();
        for(String id:ids.split(",")){for(String[]s:services){if(s[0].equals(id.trim())){if(sb.length()>0)sb.append(", ");sb.append(s[1]);}}}
        return sb.toString();
    }
    private double computeTotal(String ids){
        double t=0;for(String id:ids.split(","))for(String[]s:services)if(s[0].equals(id.trim()))t+=Double.parseDouble(s[2]);return t;
    }
    private String[]findAppointment(String id){for(String[]a:appointments)if(a[0].equals(id))return a;return null;}

    public void loadDashboard(){
        lblTotalCustomers.setText(String.valueOf(customers.size()));
        lblScheduled.setText(String.valueOf(appointments.stream().filter(a->a[5].equals("Scheduled")).count()));
        lblCompleted.setText(String.valueOf(appointments.stream().filter(a->a[5].equals("Completed")).count()));
        double rev=payments.stream().mapToDouble(p->Double.parseDouble(p[2])).sum();
        lblRevenue.setText("\u20b1"+String.format("%.0f",rev));
        DefaultTableModel m=(DefaultTableModel)dashTable.getModel();m.setRowCount(0);
        for(String[]a:appointments)m.addRow(new String[]{a[0],customerName(a[1]),stylistName(a[2]),a[3],a[4],"\u20b1"+a[7],a[5]});
    }
    public void loadAppointments(){
        DefaultTableModel m=(DefaultTableModel)apptTable.getModel();m.setRowCount(0);
        for(String[]a:appointments)m.addRow(new String[]{a[0],customerName(a[1]),stylistName(a[2]),a[3],a[4],serviceNames(a[6]),"\u20b1"+a[7],a[5]});
    }
    public void loadCustomers(){
        DefaultTableModel m=(DefaultTableModel)custTable.getModel();m.setRowCount(0);
        for(String[]c:customers)m.addRow(new String[]{c[0],c[1],c[2],c[3]});
    }
    public void loadStylists(){
        DefaultTableModel m=(DefaultTableModel)stylTable.getModel();m.setRowCount(0);
        for(String[]s:stylists)m.addRow(new String[]{s[0],s[1],s[2],s[3]});
    }
    public void loadServices(){
        DefaultTableModel m=(DefaultTableModel)svcTable.getModel();m.setRowCount(0);
        for(String[]s:services)m.addRow(new String[]{s[0],s[1],"\u20b1"+s[2],s[3]+" min"});
    }
    public void loadPayments(){
        DefaultTableModel m=(DefaultTableModel)payTable.getModel();m.setRowCount(0);
        for(String[]p:payments){String[]a=findAppointment(p[1]);String cn=a!=null?customerName(a[1]):"N/A";m.addRow(new String[]{p[0],p[1],cn,"\u20b1"+p[2],p[3],p[5]});}
    }
    public void loadApptCombos(){
        apptCustBox.removeAllItems();for(String[]c:customers)apptCustBox.addItem(c[0]+" - "+c[1]);apptCustBox.setSelectedIndex(-1);
        apptStylBox.removeAllItems();for(String[]s:stylists)if(s[3].equals("Active"))apptStylBox.addItem(s[0]+" - "+s[1]);apptStylBox.setSelectedIndex(-1);
    }
    public void loadServiceCheckboxes(){
        svcCheckPanel.removeAll();
        svcCheckPanel.setLayout(new javax.swing.BoxLayout(svcCheckPanel,javax.swing.BoxLayout.Y_AXIS));
        for(String[]s:services){JCheckBox cb=new JCheckBox(s[1]+" (\u20b1"+s[2]+", "+s[3]+" min)");cb.setName(s[0]);cb.setBackground(new java.awt.Color(255,240,245));svcCheckPanel.add(cb);}
        svcCheckPanel.revalidate();svcCheckPanel.repaint();
    }

    private void goTo(String card){((CardLayout)getContentPane().getLayout()).show(getContentPane(),card);}

    @SuppressWarnings("unchecked")
    private void initComponents(){
        loginPanel=new javax.swing.JPanel();
        dashPanel=new javax.swing.JPanel();
        apptPanel=new javax.swing.JPanel();
        custPanel=new javax.swing.JPanel();
        stylPanel=new javax.swing.JPanel();
        svcPanel=new javax.swing.JPanel();
        payPanel=new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GlamUp Beauty Salon");
        getContentPane().setLayout(new java.awt.CardLayout());

        // ── LOGIN ──────────────────────────────────────────────────
        loginPanel.setBackground(new java.awt.Color(255,230,240));
        loginPanel.setLayout(null);
        javax.swing.JLabel lblLT=new javax.swing.JLabel("\u2726 GlamUp Beauty Salon \u2726");
        lblLT.setFont(new java.awt.Font("Serif",1,28));lblLT.setForeground(new java.awt.Color(180,60,100));
        lblLT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);lblLT.setBounds(0,60,WIN_W,40);loginPanel.add(lblLT);
        javax.swing.JLabel lblLS=new javax.swing.JLabel("Appointment & Service Management System");
        lblLS.setFont(new java.awt.Font("SansSerif",0,13));lblLS.setForeground(new java.awt.Color(150,80,110));
        lblLS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);lblLS.setBounds(0,105,WIN_W,20);loginPanel.add(lblLS);
        javax.swing.JPanel lc=new javax.swing.JPanel(null);lc.setBackground(java.awt.Color.WHITE);
        lc.setBounds((WIN_W-300)/2,145,300,240);loginPanel.add(lc);
        javax.swing.JLabel le=new javax.swing.JLabel("Email:");le.setFont(new java.awt.Font("SansSerif",1,12));le.setBounds(20,30,260,20);lc.add(le);
        loginEmailTF=new javax.swing.JTextField();loginEmailTF.setBounds(20,52,260,28);lc.add(loginEmailTF);
        javax.swing.JLabel lp=new javax.swing.JLabel("Password:");lp.setFont(new java.awt.Font("SansSerif",1,12));lp.setBounds(20,90,260,20);lc.add(lp);
        loginPassTF=new javax.swing.JPasswordField();loginPassTF.setBounds(20,112,260,28);lc.add(loginPassTF);
        loginErrLbl=new javax.swing.JLabel("");loginErrLbl.setForeground(java.awt.Color.RED);loginErrLbl.setFont(new java.awt.Font("SansSerif",0,11));
        loginErrLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);loginErrLbl.setBounds(0,148,300,18);lc.add(loginErrLbl);
        loginBtn=new javax.swing.JButton("Sign In");loginBtn.setBackground(new java.awt.Color(180,60,100));loginBtn.setForeground(java.awt.Color.WHITE);
        loginBtn.setFont(new java.awt.Font("SansSerif",1,13));loginBtn.setBorder(null);loginBtn.setBounds(20,172,260,38);lc.add(loginBtn);
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){loginBtnMouseClicked(e);}});
        javax.swing.JLabel lf=new javax.swing.JLabel("GlamUp Beauty Salon \u00a9 2025 | Malolos, Bulacan");
        lf.setFont(new java.awt.Font("SansSerif",0,10));lf.setForeground(new java.awt.Color(150,100,120));
        lf.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);lf.setBounds(0,WIN_H-30,WIN_W,20);loginPanel.add(lf);
        getContentPane().add(loginPanel,"login");

        // ── DASHBOARD ─────────────────────────────────────────────
        dashPanel.setBackground(new java.awt.Color(255,245,250));dashPanel.setLayout(null);
        addHeader(dashPanel,"GlamUp Beauty Salon \u2014 Dashboard");
        addNavPanel(dashPanel,"dashboard");

        // 4 stat boxes — evenly spaced across content area
        int statW=148, statH=70, statGap=12;
        int statTotalW = 4*statW + 3*statGap;
        int statStartX = CONTENT_X + (CONTENT_W - statTotalW)/2;
        int statY = HDR_H + 15;

        javax.swing.JPanel sb1=makeStatBox("Customers",   new java.awt.Color(50,130,200));
        javax.swing.JPanel sb2=makeStatBox("Scheduled",   new java.awt.Color(180,60,100));
        javax.swing.JPanel sb3=makeStatBox("Completed",   new java.awt.Color(70,160,100));
        javax.swing.JPanel sb4=makeStatBox("Revenue",     new java.awt.Color(190,140,60));
        sb1.setBounds(statStartX,                    statY, statW, statH); dashPanel.add(sb1);
        sb2.setBounds(statStartX+  (statW+statGap),  statY, statW, statH); dashPanel.add(sb2);
        sb3.setBounds(statStartX+2*(statW+statGap),  statY, statW, statH); dashPanel.add(sb3);
        sb4.setBounds(statStartX+3*(statW+statGap),  statY, statW, statH); dashPanel.add(sb4);
        lblTotalCustomers=(javax.swing.JLabel)sb1.getComponent(0);
        lblScheduled     =(javax.swing.JLabel)sb2.getComponent(0);
        lblCompleted     =(javax.swing.JLabel)sb3.getComponent(0);
        lblRevenue       =(javax.swing.JLabel)sb4.getComponent(0);

        int dashTableY = statY + statH + 12;
        javax.swing.JLabel dtl=new javax.swing.JLabel("Recent Appointments");
        dtl.setFont(new java.awt.Font("SansSerif",1,13));dtl.setForeground(new java.awt.Color(180,60,100));
        dtl.setBounds(CONTENT_X, dashTableY, 250, 20); dashPanel.add(dtl);

        dashTable=new javax.swing.JTable();
        dashTable.setModel(new DefaultTableModel(new Object[][]{},new String[]{"ID","Customer","Stylist","Date","Time","Total","Status"}));
        dashTable.setRowHeight(24);
        int dashScrollY = dashTableY + 22;
        int dashScrollH = WIN_H - dashScrollY - 15;
        javax.swing.JScrollPane dsp=new javax.swing.JScrollPane(dashTable);
        dsp.setBounds(CONTENT_X, dashScrollY, CONTENT_W - 10, dashScrollH);
        dashPanel.add(dsp);
        getContentPane().add(dashPanel,"dashboard");

        // ── APPOINTMENTS ──────────────────────────────────────────
        apptPanel.setBackground(new java.awt.Color(255,245,250));apptPanel.setLayout(null);
        addHeader(apptPanel,"GlamUp Beauty Salon \u2014 Appointments");
        addNavPanel(apptPanel,"appointments");

        // Form panel — full content height
        javax.swing.JPanel af=new javax.swing.JPanel(null);
        af.setBackground(new java.awt.Color(255,230,240));
        af.setBounds(CONTENT_X, CONTENT_Y, FORM_W, FORM_H);
        apptPanel.add(af);

        javax.swing.JLabel aft=new javax.swing.JLabel("Book / Edit Appointment");
        aft.setFont(new java.awt.Font("SansSerif",1,13));aft.setForeground(new java.awt.Color(180,60,100));
        aft.setBounds(10,10,FORM_W-20,20);af.add(aft);

        javax.swing.JLabel lac=new javax.swing.JLabel("Customer:");lac.setBounds(10,40,100,20);af.add(lac);
        apptCustBox=new javax.swing.JComboBox<>();apptCustBox.setBounds(115,38,230,24);af.add(apptCustBox);
        javax.swing.JLabel las=new javax.swing.JLabel("Stylist:");las.setBounds(10,72,100,20);af.add(las);
        apptStylBox=new javax.swing.JComboBox<>();apptStylBox.setBounds(115,70,230,24);af.add(apptStylBox);
        javax.swing.JLabel lad=new javax.swing.JLabel("Date (YYYY-MM-DD):");lad.setBounds(10,104,150,20);af.add(lad);
        apptDateTF=new javax.swing.JTextField();apptDateTF.setBounds(165,102,180,24);af.add(apptDateTF);
        javax.swing.JLabel lat=new javax.swing.JLabel("Time (HH:MM):");lat.setBounds(10,134,150,20);af.add(lat);
        apptTimeTF=new javax.swing.JTextField();apptTimeTF.setBounds(165,132,120,24);af.add(apptTimeTF);
        javax.swing.JLabel lsv=new javax.swing.JLabel("Services:");lsv.setBounds(10,164,100,20);af.add(lsv);
        svcCheckPanel=new javax.swing.JPanel();
        svcCheckPanel.setBackground(new java.awt.Color(255,240,245));
        // Service checkboxes height: remaining space minus status row and buttons
        int svcBoxH = FORM_H - 164 - 30 - 36 - 40; // ~385 - dynamic
        javax.swing.JScrollPane ssp=new javax.swing.JScrollPane(svcCheckPanel);
        ssp.setBounds(115,162,230,Math.max(svcBoxH, 120));af.add(ssp);

        int statusY  = 164 + Math.max(svcBoxH, 120) + 8;
        int btnY     = statusY + 30;
        javax.swing.JLabel lst=new javax.swing.JLabel("Status:");lst.setBounds(10,statusY,100,20);af.add(lst);
        apptStatusBox=new javax.swing.JComboBox<>(new String[]{"Scheduled","Completed","Cancelled","No-Show"});
        apptStatusBox.setBounds(115,statusY-2,150,24);af.add(apptStatusBox);
        apptSaveBtn=new javax.swing.JButton("Save");
        apptSaveBtn.setBackground(new java.awt.Color(180,60,100));apptSaveBtn.setForeground(java.awt.Color.WHITE);
        apptSaveBtn.setBorder(null);apptSaveBtn.setBounds(10,btnY,90,28);af.add(apptSaveBtn);
        apptSaveBtn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){apptSaveBtnMouseClicked(e);}});
        apptClrBtn=new javax.swing.JButton("Clear");apptClrBtn.setBorder(null);
        apptClrBtn.setBounds(110,btnY,80,28);af.add(apptClrBtn);
        apptClrBtn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){apptClrBtnMouseClicked(e);}});

        // Table + action buttons on the right
        int apptBtnH = 30;
        int apptScrollH = TABLE_H - apptBtnH - 8;
        apptTable=new javax.swing.JTable();
        apptTable.setModel(new DefaultTableModel(new Object[][]{},new String[]{"ID","Customer","Stylist","Date","Time","Services","Total","Status"}));
        apptTable.setRowHeight(24);apptTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        apptTable.getSelectionModel().addListSelectionListener(e->apptTableSelected());
        javax.swing.JScrollPane asp=new javax.swing.JScrollPane(apptTable);
        asp.setBounds(TABLE_X, CONTENT_Y, TABLE_W, apptScrollH);
        apptPanel.add(asp);

        int apptBtnY = CONTENT_Y + apptScrollH + 8;
        apptPayBtn=new javax.swing.JButton("Record Payment");
        apptPayBtn.setBackground(new java.awt.Color(70,160,100));apptPayBtn.setForeground(java.awt.Color.WHITE);
        apptPayBtn.setBorder(null);apptPayBtn.setBounds(TABLE_X, apptBtnY, 155, apptBtnH);
        apptPanel.add(apptPayBtn);
        apptPayBtn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){apptPayBtnMouseClicked(e);}});

        apptCancelBtn=new javax.swing.JButton("Cancel Appointment");
        apptCancelBtn.setBackground(new java.awt.Color(200,60,60));apptCancelBtn.setForeground(java.awt.Color.WHITE);
        apptCancelBtn.setBorder(null);apptCancelBtn.setBounds(TABLE_X+165, apptBtnY, 175, apptBtnH);
        apptPanel.add(apptCancelBtn);
        apptCancelBtn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){apptCancelBtnMouseClicked(e);}});
        getContentPane().add(apptPanel,"appointments");

        // ── CUSTOMERS ─────────────────────────────────────────────
        custPanel.setBackground(new java.awt.Color(255,245,250));custPanel.setLayout(null);
        addHeader(custPanel,"GlamUp Beauty Salon \u2014 Customers");
        addNavPanel(custPanel,"customers");

        javax.swing.JPanel cf=new javax.swing.JPanel(null);
        cf.setBackground(new java.awt.Color(255,230,240));
        cf.setBounds(CONTENT_X, CONTENT_Y, FORM_W, FORM_H);
        custPanel.add(cf);

        javax.swing.JLabel cft=new javax.swing.JLabel("Customer Information");
        cft.setFont(new java.awt.Font("SansSerif",1,13));cft.setForeground(new java.awt.Color(180,60,100));
        cft.setBounds(10,10,FORM_W-20,20);cf.add(cft);
        javax.swing.JLabel lcn=new javax.swing.JLabel("Full Name:");lcn.setBounds(10,40,110,20);cf.add(lcn);
        custNameTF=new javax.swing.JTextField();custNameTF.setBounds(125,38,220,24);cf.add(custNameTF);
        javax.swing.JLabel lcp=new javax.swing.JLabel("Contact #:");lcp.setBounds(10,72,110,20);cf.add(lcp);
        custPhoneTF=new javax.swing.JTextField();custPhoneTF.setBounds(125,70,220,24);cf.add(custPhoneTF);
        javax.swing.JLabel lce=new javax.swing.JLabel("Email:");lce.setBounds(10,104,110,20);cf.add(lce);
        custEmailTF=new javax.swing.JTextField();custEmailTF.setBounds(125,102,220,24);cf.add(custEmailTF);
        custSaveBtn=new javax.swing.JButton("Save");
        custSaveBtn.setBackground(new java.awt.Color(180,60,100));custSaveBtn.setForeground(java.awt.Color.WHITE);
        custSaveBtn.setBorder(null);custSaveBtn.setBounds(10,142,90,28);cf.add(custSaveBtn);
        custSaveBtn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){custSaveBtnMouseClicked(e);}});
        custClrBtn=new javax.swing.JButton("Clear");custClrBtn.setBorder(null);
        custClrBtn.setBounds(110,142,80,28);cf.add(custClrBtn);
        custClrBtn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){custClrBtnMouseClicked(e);}});
        custDelBtn=new javax.swing.JButton("Delete Selected");
        custDelBtn.setBackground(new java.awt.Color(200,60,60));custDelBtn.setForeground(java.awt.Color.WHITE);
        custDelBtn.setBorder(null);custDelBtn.setBounds(200,142,150,28);cf.add(custDelBtn);
        custDelBtn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){custDelBtnMouseClicked(e);}});

        custTable=new javax.swing.JTable();
        custTable.setModel(new DefaultTableModel(new Object[][]{},new String[]{"ID","Full Name","Contact Number","Email"}));
        custTable.setRowHeight(24);custTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        custTable.getSelectionModel().addListSelectionListener(e->custTableSelected());
        javax.swing.JScrollPane csp=new javax.swing.JScrollPane(custTable);
        csp.setBounds(TABLE_X, CONTENT_Y, TABLE_W, TABLE_H);
        custPanel.add(csp);
        getContentPane().add(custPanel,"customers");

        // ── STYLISTS ──────────────────────────────────────────────
        stylPanel.setBackground(new java.awt.Color(255,245,250));stylPanel.setLayout(null);
        addHeader(stylPanel,"GlamUp Beauty Salon \u2014 Stylists");
        addNavPanel(stylPanel,"stylists");

        javax.swing.JPanel stf=new javax.swing.JPanel(null);
        stf.setBackground(new java.awt.Color(255,230,240));
        stf.setBounds(CONTENT_X, CONTENT_Y, FORM_W, FORM_H);
        stylPanel.add(stf);

        javax.swing.JLabel stft=new javax.swing.JLabel("Stylist Information");
        stft.setFont(new java.awt.Font("SansSerif",1,13));stft.setForeground(new java.awt.Color(180,60,100));
        stft.setBounds(10,10,FORM_W-20,20);stf.add(stft);
        javax.swing.JLabel lsn=new javax.swing.JLabel("Full Name:");lsn.setBounds(10,40,110,20);stf.add(lsn);
        stylNameTF=new javax.swing.JTextField();stylNameTF.setBounds(125,38,220,24);stf.add(stylNameTF);
        javax.swing.JLabel lsp=new javax.swing.JLabel("Specialty:");lsp.setBounds(10,72,110,20);stf.add(lsp);
        stylSpecTF=new javax.swing.JTextField();stylSpecTF.setBounds(125,70,220,24);stf.add(stylSpecTF);
        javax.swing.JLabel lss=new javax.swing.JLabel("Status:");lss.setBounds(10,104,110,20);stf.add(lss);
        stylStatusBox=new javax.swing.JComboBox<>(new String[]{"Active","Inactive"});
        stylStatusBox.setBounds(125,102,150,24);stf.add(stylStatusBox);
        stylSaveBtn=new javax.swing.JButton("Save");
        stylSaveBtn.setBackground(new java.awt.Color(180,60,100));stylSaveBtn.setForeground(java.awt.Color.WHITE);
        stylSaveBtn.setBorder(null);stylSaveBtn.setBounds(10,142,90,28);stf.add(stylSaveBtn);
        stylSaveBtn.addMouseListener(new java.awt.event.MouseAdapter(){@Override
        public void mouseClicked(java.awt.event.MouseEvent e){stylSaveBtnMouseClicked(e);}});
        stylClrBtn=new javax.swing.JButton("Clear");stylClrBtn.setBorder(null);
        stylClrBtn.setBounds(110,142,80,28);stf.add(stylClrBtn);
        stylClrBtn.addMouseListener(new java.awt.event.MouseAdapter(){@Override
        public void mouseClicked(java.awt.event.MouseEvent e){stylClrBtnMouseClicked(e);}});
        stylDelBtn=new javax.swing.JButton("Delete Selected");
        stylDelBtn.setBackground(new java.awt.Color(200,60,60));stylDelBtn.setForeground(java.awt.Color.WHITE);
        stylDelBtn.setBorder(null);stylDelBtn.setBounds(200,142,150,28);stf.add(stylDelBtn);
        stylDelBtn.addMouseListener(new java.awt.event.MouseAdapter(){@Override
        public void mouseClicked(java.awt.event.MouseEvent e){stylDelBtnMouseClicked(e);}});

        stylTable=new javax.swing.JTable();
        stylTable.setModel(new DefaultTableModel(new Object[][]{},new String[]{"ID","Full Name","Specialty","Status"}));
        stylTable.setRowHeight(24);stylTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        stylTable.getSelectionModel().addListSelectionListener(e->stylTableSelected());
        javax.swing.JScrollPane stsp=new javax.swing.JScrollPane(stylTable);
        stsp.setBounds(TABLE_X, CONTENT_Y, TABLE_W, TABLE_H);
        stylPanel.add(stsp);
        getContentPane().add(stylPanel,"stylists");

        // ── SERVICES ──────────────────────────────────────────────
        svcPanel.setBackground(new java.awt.Color(255,245,250));svcPanel.setLayout(null);
        addHeader(svcPanel,"GlamUp Beauty Salon \u2014 Services");
        addNavPanel(svcPanel,"services");

        javax.swing.JPanel svf=new javax.swing.JPanel(null);
        svf.setBackground(new java.awt.Color(255,230,240));
        svf.setBounds(CONTENT_X, CONTENT_Y, FORM_W, FORM_H);
        svcPanel.add(svf);

        javax.swing.JLabel svft=new javax.swing.JLabel("Service Information");
        svft.setFont(new java.awt.Font("SansSerif",1,13));svft.setForeground(new java.awt.Color(180,60,100));
        svft.setBounds(10,10,FORM_W-20,20);svf.add(svft);
        javax.swing.JLabel lsvn=new javax.swing.JLabel("Service Name:");lsvn.setBounds(10,40,120,20);svf.add(lsvn);
        svcNameTF=new javax.swing.JTextField();svcNameTF.setBounds(140,38,205,24);svf.add(svcNameTF);
        javax.swing.JLabel lsvp=new javax.swing.JLabel("Price (\u20b1):");lsvp.setBounds(10,72,120,20);svf.add(lsvp);
        svcPriceTF=new javax.swing.JTextField();svcPriceTF.setBounds(140,70,205,24);svf.add(svcPriceTF);
        javax.swing.JLabel lsvd=new javax.swing.JLabel("Duration (min):");lsvd.setBounds(10,104,120,20);svf.add(lsvd);
        svcDurTF=new javax.swing.JTextField();svcDurTF.setBounds(140,102,205,24);svf.add(svcDurTF);
        svcSaveBtn=new javax.swing.JButton("Save");
        svcSaveBtn.setBackground(new java.awt.Color(180,60,100));svcSaveBtn.setForeground(java.awt.Color.WHITE);
        svcSaveBtn.setBorder(null);svcSaveBtn.setBounds(10,142,90,28);svf.add(svcSaveBtn);
        svcSaveBtn.addMouseListener(new java.awt.event.MouseAdapter(){@Override
        public void mouseClicked(java.awt.event.MouseEvent e){svcSaveBtnMouseClicked(e);}});
        svcClrBtn=new javax.swing.JButton("Clear");svcClrBtn.setBorder(null);
        svcClrBtn.setBounds(110,142,80,28);svf.add(svcClrBtn);
        svcClrBtn.addMouseListener(new java.awt.event.MouseAdapter(){@Override
        public void mouseClicked(java.awt.event.MouseEvent e){svcClrBtnMouseClicked(e);}});
        svcDelBtn=new javax.swing.JButton("Delete Selected");
        svcDelBtn.setBackground(new java.awt.Color(200,60,60));svcDelBtn.setForeground(java.awt.Color.WHITE);
        svcDelBtn.setBorder(null);svcDelBtn.setBounds(200,142,150,28);svf.add(svcDelBtn);
        svcDelBtn.addMouseListener(new java.awt.event.MouseAdapter(){@Override
        public void mouseClicked(java.awt.event.MouseEvent e){svcDelBtnMouseClicked(e);}});

        svcTable=new javax.swing.JTable();
        svcTable.setModel(new DefaultTableModel(new Object[][]{},new String[]{"ID","Service Name","Price","Duration"}));
        svcTable.setRowHeight(24);svcTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        svcTable.getSelectionModel().addListSelectionListener(e->svcTableSelected());
        javax.swing.JScrollPane svsp=new javax.swing.JScrollPane(svcTable);
        svsp.setBounds(TABLE_X, CONTENT_Y, TABLE_W, TABLE_H);
        svcPanel.add(svsp);
        getContentPane().add(svcPanel,"services");

        // ── PAYMENTS ──────────────────────────────────────────────
        payPanel.setBackground(new java.awt.Color(255,245,250));payPanel.setLayout(null);
        addHeader(payPanel,"GlamUp Beauty Salon \u2014 Payments");
        addNavPanel(payPanel,"payments");

        javax.swing.JLabel ptl=new javax.swing.JLabel("Payment Records");
        ptl.setFont(new java.awt.Font("SansSerif",1,14));ptl.setForeground(new java.awt.Color(180,60,100));
        ptl.setBounds(CONTENT_X, CONTENT_Y - 2, 300, 24);
        payPanel.add(ptl);

        payTable=new javax.swing.JTable();
        payTable.setModel(new DefaultTableModel(new Object[][]{},new String[]{"Pay ID","Appt ID","Customer","Amount","Method","Date"}));
        payTable.setRowHeight(24);
        javax.swing.JScrollPane psp=new javax.swing.JScrollPane(payTable);
        // Payments has no form panel, so table spans full content width
        psp.setBounds(CONTENT_X, CONTENT_Y + 26, CONTENT_W - 10, TABLE_H - 26);
        payPanel.add(psp);
        getContentPane().add(payPanel,"payments");

        setSize(WIN_W, WIN_H);
        setLocationRelativeTo(null);
    }

    private void addHeader(javax.swing.JPanel panel,String title){
        javax.swing.JLabel h=new javax.swing.JLabel(title);
        h.setBackground(new java.awt.Color(180,60,100));h.setForeground(java.awt.Color.WHITE);
        h.setFont(new java.awt.Font("Serif",1,18));h.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        h.setOpaque(true);h.setBounds(0,0,WIN_W,HDR_H);panel.add(h);
        javax.swing.JButton lb=new javax.swing.JButton("Logout");
        lb.setBackground(new java.awt.Color(200,60,60));lb.setForeground(java.awt.Color.WHITE);
        lb.setBorder(null);lb.setBounds(WIN_W-120,13,100,28);panel.add(lb);
        lb.addMouseListener(new java.awt.event.MouseAdapter(){@Override
        public void mouseClicked(java.awt.event.MouseEvent e){
            currentUser=null;loginEmailTF.setText("");loginPassTF.setText("");loginErrLbl.setText("");goTo("login");
        }});
    }

    private void addNavPanel(javax.swing.JPanel parent,String activeCard){
        javax.swing.JPanel nav=new javax.swing.JPanel(null);
        nav.setBackground(new java.awt.Color(50,30,40));
        nav.setBounds(0, HDR_H, NAV_W, WIN_H - HDR_H);
        parent.add(nav);
        javax.swing.JLabel nl=new javax.swing.JLabel("Navigation");
        nl.setForeground(new java.awt.Color(200,160,180));nl.setFont(new java.awt.Font("SansSerif",0,12));
        nl.setBounds(15,15,130,20);nav.add(nl);
        String[][]items={{"Dashboard","dashboard"},{"Appointments","appointments"},{"Customers","customers"},{"Stylists","stylists"},{"Services","services"},{"Payments","payments"}};
        int y=50;
        for(String[]item:items){
            final String card=item[1];
            javax.swing.JButton btn=new javax.swing.JButton(item[0]);
            btn.setBorder(null);btn.setFocusPainted(false);btn.setFont(new java.awt.Font("SansSerif",1,12));
            if(card.equals(activeCard)){btn.setBackground(new java.awt.Color(180,60,100));btn.setForeground(java.awt.Color.WHITE);}
            else{btn.setBackground(new java.awt.Color(50,30,40));btn.setForeground(new java.awt.Color(200,160,180));}
            btn.setBounds(0,y,NAV_W,42);nav.add(btn);
            btn.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){navBtnClicked(card);}});
            y+=50;
        }
    }

    private void navBtnClicked(String card){
        goTo(card);
        switch(card){
            case "dashboard":    loadDashboard();break;
            case "appointments": loadAppointments();loadApptCombos();loadServiceCheckboxes();break;
            case "customers":    loadCustomers();break;
            case "stylists":     loadStylists();break;
            case "services":     loadServices();break;
            case "payments":     loadPayments();break;
        }
    }

    private javax.swing.JPanel makeStatBox(String label,java.awt.Color color){
        javax.swing.JPanel box=new javax.swing.JPanel(null);box.setBackground(java.awt.Color.WHITE);
        box.setBorder(javax.swing.BorderFactory.createMatteBorder(0,4,0,0,color));
        javax.swing.JLabel vl=new javax.swing.JLabel("0");vl.setFont(new java.awt.Font("Serif",1,22));vl.setForeground(color);vl.setBounds(12,10,130,28);box.add(vl);
        javax.swing.JLabel nl=new javax.swing.JLabel(label);nl.setFont(new java.awt.Font("SansSerif",0,11));nl.setForeground(new java.awt.Color(120,90,105));nl.setBounds(12,38,130,18);box.add(nl);
        return box;
    }

    // ── LOGIN ──────────────────────────────────────────────────────
    private void loginBtnMouseClicked(java.awt.event.MouseEvent evt){
        String email=loginEmailTF.getText().trim();
        String pass=new String(loginPassTF.getPassword()).trim();
        boolean found=false;
        for(String[]u:users){if(u[2].equalsIgnoreCase(email)&&u[3].equals(pass)){currentUser=u;found=true;break;}}
        if(!found){loginErrLbl.setText("Invalid email or password.");}
        else{loginErrLbl.setText("");goTo("dashboard");loadDashboard();}
    }

    // ── APPOINTMENTS ───────────────────────────────────────────────
    private String editingApptId=null;
    private void apptTableSelected(){
        int row=apptTable.getSelectedRow();if(row<0)return;
        DefaultTableModel m=(DefaultTableModel)apptTable.getModel();
        editingApptId=m.getValueAt(row,0).toString();
        String[]a=findAppointment(editingApptId);if(a==null)return;
        for(int i=0;i<apptCustBox.getItemCount();i++)if(apptCustBox.getItemAt(i).startsWith(a[1])){apptCustBox.setSelectedIndex(i);break;}
        for(int i=0;i<apptStylBox.getItemCount();i++)if(apptStylBox.getItemAt(i).startsWith(a[2])){apptStylBox.setSelectedIndex(i);break;}
        apptDateTF.setText(a[3]);apptTimeTF.setText(a[4]);apptStatusBox.setSelectedItem(a[5]);
        for(java.awt.Component c:svcCheckPanel.getComponents())if(c instanceof JCheckBox){JCheckBox cb=(JCheckBox)c;cb.setSelected(a[6].contains(cb.getName()));}
    }
    private void apptSaveBtnMouseClicked(java.awt.event.MouseEvent evt){
        if(apptCustBox.getSelectedIndex()==-1||apptStylBox.getSelectedIndex()==-1){JOptionPane.showMessageDialog(this,"Please select a customer and stylist.");return;}
        String date=apptDateTF.getText().trim(),time=apptTimeTF.getText().trim();
        if(date.isEmpty()||time.isEmpty()){JOptionPane.showMessageDialog(this,"Date and Time are required.");return;}
        StringBuilder sids=new StringBuilder();
        for(java.awt.Component c:svcCheckPanel.getComponents())if(c instanceof JCheckBox&&((JCheckBox)c).isSelected()){if(sids.length()>0)sids.append(",");sids.append(((JCheckBox)c).getName());}
        if(sids.length()==0){JOptionPane.showMessageDialog(this,"Select at least one service.");return;}
        String custId=apptCustBox.getSelectedItem().toString().split(" - ")[0].trim();
        String stylId=apptStylBox.getSelectedItem().toString().split(" - ")[0].trim();
        String status=apptStatusBox.getSelectedItem().toString();
        int total=(int)computeTotal(sids.toString());
        if(editingApptId==null){
            appointments.add(new String[]{"A"+apptSeq++,custId,stylId,date,time,status,sids.toString(),String.valueOf(total),currentUser[0]});
            JOptionPane.showMessageDialog(this,"Appointment booked successfully!");
        }else{
            String[]a=findAppointment(editingApptId);
            if(a!=null){a[1]=custId;a[2]=stylId;a[3]=date;a[4]=time;a[5]=status;a[6]=sids.toString();a[7]=String.valueOf(total);}
            JOptionPane.showMessageDialog(this,"Appointment updated!");editingApptId=null;
        }
        loadAppointments();apptClrBtnMouseClicked(null);
    }
    private void apptClrBtnMouseClicked(java.awt.event.MouseEvent evt){
        apptCustBox.setSelectedIndex(-1);apptStylBox.setSelectedIndex(-1);apptDateTF.setText("");apptTimeTF.setText("");apptStatusBox.setSelectedIndex(0);
        for(java.awt.Component c:svcCheckPanel.getComponents())if(c instanceof JCheckBox)((JCheckBox)c).setSelected(false);
        editingApptId=null;apptTable.clearSelection();
    }
    private void apptPayBtnMouseClicked(java.awt.event.MouseEvent evt){
        int row=apptTable.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select an appointment first.");return;}
        String id=apptTable.getValueAt(row,0).toString();String[]a=findAppointment(id);if(a==null)return;
        if(a[5].equals("Completed")){JOptionPane.showMessageDialog(this,"Already paid.");return;}
        if(a[5].equals("Cancelled")){JOptionPane.showMessageDialog(this,"Cannot pay a cancelled appointment.");return;}
        String[]methods={"Cash","GCash","Card"};
        String method=(String)JOptionPane.showInputDialog(this,"Payment for: "+id+"\nTotal: \u20b1"+a[7],"Record Payment",JOptionPane.PLAIN_MESSAGE,null,methods,"Cash");
        if(method!=null){
            String date=new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            payments.add(new String[]{"P"+paySeq++,a[0],a[7],method,"Paid",date});
            a[5]="Completed";JOptionPane.showMessageDialog(this,"Payment recorded! Appointment marked Completed.");loadAppointments();
        }
    }
    private void apptCancelBtnMouseClicked(java.awt.event.MouseEvent evt){
        int row=apptTable.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select an appointment first.");return;}
        String id=apptTable.getValueAt(row,0).toString();String[]a=findAppointment(id);if(a==null)return;
        int c=JOptionPane.showConfirmDialog(this,"Cancel appointment "+id+"?","Confirm",JOptionPane.YES_NO_OPTION);
        if(c==JOptionPane.YES_OPTION){a[5]="Cancelled";JOptionPane.showMessageDialog(this,"Appointment cancelled.");loadAppointments();}
    }

    // ── CUSTOMERS ──────────────────────────────────────────────────
    private String editingCustId=null;
    private void custTableSelected(){
        int row=custTable.getSelectedRow();if(row<0)return;
        DefaultTableModel m=(DefaultTableModel)custTable.getModel();editingCustId=m.getValueAt(row,0).toString();
        custNameTF.setText(m.getValueAt(row,1).toString());custPhoneTF.setText(m.getValueAt(row,2).toString());custEmailTF.setText(m.getValueAt(row,3).toString());
    }
    private void custSaveBtnMouseClicked(java.awt.event.MouseEvent evt){
        String name=custNameTF.getText().trim(),phone=custPhoneTF.getText().trim(),email=custEmailTF.getText().trim();
        if(name.isEmpty()||phone.isEmpty()||email.isEmpty()){JOptionPane.showMessageDialog(this,"All fields are required.");return;}
        if(editingCustId==null){customers.add(new String[]{"C"+custSeq++,name,phone,email});JOptionPane.showMessageDialog(this,"Customer added!");}
        else{for(String[]c:customers)if(c[0].equals(editingCustId)){c[1]=name;c[2]=phone;c[3]=email;break;}JOptionPane.showMessageDialog(this,"Customer updated!");editingCustId=null;}
        loadCustomers();custClrBtnMouseClicked(null);
    }
    private void custClrBtnMouseClicked(java.awt.event.MouseEvent evt){custNameTF.setText("");custPhoneTF.setText("");custEmailTF.setText("");editingCustId=null;custTable.clearSelection();}
    private void custDelBtnMouseClicked(java.awt.event.MouseEvent evt){
        int row=custTable.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a customer first.");return;}
        String id=custTable.getValueAt(row,0).toString();
        if(JOptionPane.showConfirmDialog(this,"Delete customer "+id+"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){customers.removeIf(c->c[0].equals(id));loadCustomers();custClrBtnMouseClicked(null);}
    }

    // ── STYLISTS ───────────────────────────────────────────────────
    private String editingStylId=null;
    private void stylTableSelected(){
        int row=stylTable.getSelectedRow();if(row<0)return;
        DefaultTableModel m=(DefaultTableModel)stylTable.getModel();editingStylId=m.getValueAt(row,0).toString();
        stylNameTF.setText(m.getValueAt(row,1).toString());stylSpecTF.setText(m.getValueAt(row,2).toString());stylStatusBox.setSelectedItem(m.getValueAt(row,3).toString());
    }
    private void stylSaveBtnMouseClicked(java.awt.event.MouseEvent evt){
        String name=stylNameTF.getText().trim(),spec=stylSpecTF.getText().trim(),status=stylStatusBox.getSelectedItem().toString();
        if(name.isEmpty()||spec.isEmpty()){JOptionPane.showMessageDialog(this,"All fields are required.");return;}
        if(editingStylId==null){stylists.add(new String[]{"S"+stylSeq++,name,spec,status});JOptionPane.showMessageDialog(this,"Stylist added!");}
        else{for(String[]s:stylists)if(s[0].equals(editingStylId)){s[1]=name;s[2]=spec;s[3]=status;break;}JOptionPane.showMessageDialog(this,"Stylist updated!");editingStylId=null;}
        loadStylists();stylClrBtnMouseClicked(null);
    }
    private void stylClrBtnMouseClicked(java.awt.event.MouseEvent evt){stylNameTF.setText("");stylSpecTF.setText("");stylStatusBox.setSelectedIndex(0);editingStylId=null;stylTable.clearSelection();}
    private void stylDelBtnMouseClicked(java.awt.event.MouseEvent evt){
        int row=stylTable.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a stylist first.");return;}
        String id=stylTable.getValueAt(row,0).toString();
        if(JOptionPane.showConfirmDialog(this,"Delete stylist "+id+"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){stylists.removeIf(s->s[0].equals(id));loadStylists();stylClrBtnMouseClicked(null);}
    }

    // ── SERVICES ───────────────────────────────────────────────────
    private String editingSvcId=null;
    private void svcTableSelected(){
        int row=svcTable.getSelectedRow();if(row<0)return;
        DefaultTableModel m=(DefaultTableModel)svcTable.getModel();editingSvcId=m.getValueAt(row,0).toString();
        svcNameTF.setText(m.getValueAt(row,1).toString());
        svcPriceTF.setText(m.getValueAt(row,2).toString().replace("\u20b1",""));
        svcDurTF.setText(m.getValueAt(row,3).toString().replace(" min",""));
    }
    private void svcSaveBtnMouseClicked(java.awt.event.MouseEvent evt){
        String name=svcNameTF.getText().trim(),price=svcPriceTF.getText().trim(),dur=svcDurTF.getText().trim();
        if(name.isEmpty()||price.isEmpty()||dur.isEmpty()){JOptionPane.showMessageDialog(this,"All fields are required.");return;}
        try{Double.parseDouble(price);Integer.parseInt(dur);}catch(NumberFormatException ex){JOptionPane.showMessageDialog(this,"Price and Duration must be numbers.");return;}
        if(editingSvcId==null){services.add(new String[]{"SV"+svcSeq++,name,price,dur});JOptionPane.showMessageDialog(this,"Service added!");}
        else{for(String[]s:services)if(s[0].equals(editingSvcId)){s[1]=name;s[2]=price;s[3]=dur;break;}JOptionPane.showMessageDialog(this,"Service updated!");editingSvcId=null;}
        loadServices();svcClrBtnMouseClicked(null);
    }
    private void svcClrBtnMouseClicked(java.awt.event.MouseEvent evt){svcNameTF.setText("");svcPriceTF.setText("");svcDurTF.setText("");editingSvcId=null;svcTable.clearSelection();}
    private void svcDelBtnMouseClicked(java.awt.event.MouseEvent evt){
        int row=svcTable.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a service first.");return;}
        String id=svcTable.getValueAt(row,0).toString();
        if(JOptionPane.showConfirmDialog(this,"Delete service "+id+"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){services.removeIf(s->s[0].equals(id));loadServices();svcClrBtnMouseClicked(null);}
    }

    public static void main(String args[]){
        try{for(javax.swing.UIManager.LookAndFeelInfo info:javax.swing.UIManager.getInstalledLookAndFeels())if("Nimbus".equals(info.getName())){javax.swing.UIManager.setLookAndFeel(info.getClassName());break;}}catch(Exception ex){}
        java.awt.EventQueue.invokeLater(()->new NewClass().setVisible(true));
    }

    // Variables
    private javax.swing.JPanel loginPanel,dashPanel,apptPanel,custPanel,stylPanel,svcPanel,payPanel;
    private javax.swing.JTextField loginEmailTF;
    private javax.swing.JPasswordField loginPassTF;
    private javax.swing.JLabel loginErrLbl;
    private javax.swing.JButton loginBtn;
    private javax.swing.JLabel lblTotalCustomers,lblScheduled,lblCompleted,lblRevenue;
    private javax.swing.JTable dashTable,apptTable,custTable,stylTable,svcTable,payTable;
    private javax.swing.JComboBox<String> apptCustBox,apptStylBox,apptStatusBox,stylStatusBox;
    private javax.swing.JTextField apptDateTF,apptTimeTF,custNameTF,custPhoneTF,custEmailTF,stylNameTF,stylSpecTF,svcNameTF,svcPriceTF,svcDurTF;
    private javax.swing.JPanel svcCheckPanel;
    private javax.swing.JButton apptSaveBtn,apptClrBtn,apptPayBtn,apptCancelBtn,custSaveBtn,custClrBtn,custDelBtn,stylSaveBtn,stylClrBtn,stylDelBtn,svcSaveBtn,svcClrBtn,svcDelBtn;
}
