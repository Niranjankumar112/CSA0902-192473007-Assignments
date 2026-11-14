# Task 05 – Race Conditions in Cinema Seat Booking

Run: `javac SeatBooking.java` then `java SeatBooking`.

The synchronized `book` method makes checking and marking a seat one atomic critical section. Expected output is `Seats sold: 10`. To satisfy the comparison, create an unsynchronized copy, run it repeatedly, then compare with synchronized method and synchronized-block versions.

**Measurements:** Record elapsed time for 10,000 booking attempts for each version. Timing depends on the laptop and scheduling; fill the CSV with actual runs. Discuss intrinsic monitor locking and when static synchronization would be appropriate.
