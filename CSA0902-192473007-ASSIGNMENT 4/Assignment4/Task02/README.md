# Task 2 – Hospital Outpatient Appointment Desk
Event table:
| Source | Event | Listener | Action |
|---|---|---|---|
| Department JComboBox | ItemEvent | ItemListener | Refresh doctors from cached map |
| Book JButton | ActionEvent | ActionListener | Validate and book |
| Window close | WindowEvent | WindowAdapter | Confirm if booking is in progress |
| Time-slot JComboBox | ItemEvent | ItemListener | Update selected slot |

Component tree: JFrame(BorderLayout) → form JPanel(GridLayout: patient ID, department, doctor, time slot); action JPanel(FlowLayout: Book); status JLabel(SOUTH).

SIMATS-style pseudocode:
```
ALGORITHM RefreshDoctors
INPUT selectedDepartment
BEGIN
 doctorCombo.removeAllItems()
 FOR EACH doctor IN doctorsByDepartment.get(selectedDepartment) DO
  doctorCombo.addItem(doctor)
 END FOR
 IF doctorCombo.getItemCount() > 0 THEN doctorCombo.setSelectedIndex(0) END IF
END
```
```
ALGORITHM BookAppointment
BEGIN
 IF patientId is empty OR department/doctor/slot is not selected THEN
  DISPLAY validation error
  RETURN
 END IF
 SET bookingInProgress TO TRUE
 TRY
  SAVE appointment through service
  DISPLAY confirmation
 CATCH booking error
  DISPLAY error message
 FINALLY
  SET bookingInProgress TO FALSE
 END TRY
END
```
Trace: construct frame → initialize components/listeners → visible → department ItemEvent → refresh doctors → select doctor/slot → Book ActionEvent → validate/save → confirmation → windowClosing → confirm if booking in progress → dispose/windowClosed. Applet lifecycle methods are init/start/paint/stop/destroy; JFrame uses window and component events.
Alternative: cached Map setup O(d+m) time/space; lookup O(1) average plus O(k) to populate k doctors. Fetching every change adds database/network latency and less local cache memory.
