export interface Reservation {
  id?: string;
  reservationDate: string; // ISO date string
  timeFrom: string; // HH:MM format
  timeTo: string; // HH:MM format
  roomNumber: number;
  comment: string;
  participants: Participant[];
  privateCode?: string;
  publicCode?: string;
}

export interface Participant {
  id?: string;
  firstName: string;
  lastName: string;
}

