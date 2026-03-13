import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Card, CardBody, Table, TableHeader, TableColumn, TableBody, TableRow, TableCell } from '@nextui-org/react';
import { Reservation } from '../domain/Reservation';

export const ReservationsListScreen: React.FC = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [reservations, setReservations] = useState<Reservation[]>([]);

  useEffect(() => {
    fetchReservations();
  }, []);

  const fetchReservations = async () => {
    try {
      const response = await fetch('http://localhost:8081/api/reservations');
      if (response.ok) {
        const data = await response.json();
        setReservations(data);
      }
    } catch (err) {
      console.error('Fehler beim Laden der Reservationen', err);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('de-CH', { 
      day: '2-digit', 
      month: '2-digit', 
      year: 'numeric' 
    });
  };

  const formatTime = (timeString: string) => {
    return timeString.substring(0, 5); // HH:MM
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
        <div className="text-center">
          <div className="text-2xl mb-4">Laden...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
      <div className="max-w-6xl mx-auto">
        <Card className="shadow-xl">
          <CardBody className="p-6 space-y-6">
            <div className="flex items-center justify-between">
              <h1 className="text-2xl font-bold text-gray-800">Alle Reservationen</h1>
              <Button
                variant="light"
                size="sm"
                onClick={() => navigate('/')}
              >
                Zur Startseite
              </Button>
            </div>

            {reservations.length === 0 ? (
              <div className="text-center py-8">
                <p className="text-gray-600">Keine Reservationen gefunden</p>
                <Button
                  color="primary"
                  className="mt-4"
                  onClick={() => navigate('/create')}
                >
                  Erste Reservation erstellen
                </Button>
              </div>
            ) : (
              <div className="overflow-x-auto">
                <Table aria-label="Reservations table">
                  <TableHeader>
                    <TableColumn>Datum</TableColumn>
                    <TableColumn>Zeit</TableColumn>
                    <TableColumn>Zimmer</TableColumn>
                    <TableColumn>Bemerkung</TableColumn>
                    <TableColumn>Teilnehmer</TableColumn>
                  </TableHeader>
                  <TableBody>
                    {reservations.map((reservation) => (
                      <TableRow key={reservation.id}>
                        <TableCell>{formatDate(reservation.reservationDate)}</TableCell>
                        <TableCell>
                          {formatTime(reservation.timeFrom)} - {formatTime(reservation.timeTo)}
                        </TableCell>
                        <TableCell>Zimmer {reservation.roomNumber}</TableCell>
                        <TableCell className="max-w-xs truncate">
                          {reservation.comment}
                        </TableCell>
                        <TableCell>
                          {reservation.participants.map((p, i) => (
                            <span key={i}>
                              {p.firstName} {p.lastName}
                              {i < reservation.participants.length - 1 && ', '}
                            </span>
                          ))}
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </div>
            )}
          </CardBody>
        </Card>
      </div>
    </div>
  );
};

