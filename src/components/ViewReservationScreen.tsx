import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Card, CardBody } from '@nextui-org/react';
import { Reservation } from '../domain/Reservation';

export const ViewReservationScreen: React.FC = () => {
  const navigate = useNavigate();
  const { code } = useParams<{ code: string }>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [reservation, setReservation] = useState<Reservation | null>(null);

  useEffect(() => {
    if (code) {
      fetchReservation();
    }
  }, [code]);

  const fetchReservation = async () => {
    try {
      const response = await fetch(`http://localhost:8081/api/reservations/public/${code}`);
      if (!response.ok) {
        setError('Reservation nicht gefunden');
        setLoading(false);
        return;
      }
      const data = await response.json();
      setReservation(data);
      setLoading(false);
    } catch (err) {
      setError('Fehler beim Laden der Reservation');
      setLoading(false);
    }
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

  if (error || !reservation) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
        <Card className="max-w-md w-full">
          <CardBody className="p-6 text-center space-y-4">
            <div className="text-4xl">❌</div>
            <h2 className="text-xl font-bold text-gray-800">{error || 'Reservation nicht gefunden'}</h2>
            <Button onClick={() => navigate('/')}>Zur Startseite</Button>
          </CardBody>
        </Card>
      </div>
    );
  }

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('de-CH', { 
      weekday: 'long', 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    });
  };

  const formatTime = (timeString: string) => {
    return timeString.substring(0, 5); // HH:MM
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
      <div className="max-w-2xl mx-auto">
        <Card className="shadow-xl">
          <CardBody className="p-6 space-y-6">
            <div className="flex items-center justify-between">
              <h1 className="text-2xl font-bold text-gray-800">Reservation Details</h1>
              <Button
                variant="light"
                size="sm"
                onClick={() => navigate('/')}
              >
                Zurück
              </Button>
            </div>

            <div className="space-y-4">
              <div className="bg-gray-50 p-4 rounded-lg">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm text-gray-600">Datum</p>
                    <p className="font-semibold">{formatDate(reservation.reservationDate)}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Zeit</p>
                    <p className="font-semibold">
                      {formatTime(reservation.timeFrom)} - {formatTime(reservation.timeTo)}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Zimmer</p>
                    <p className="font-semibold">Zimmer {reservation.roomNumber}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Teilnehmer</p>
                    <p className="font-semibold">{reservation.participants.length}</p>
                  </div>
                </div>
              </div>

              <div>
                <p className="text-sm text-gray-600 mb-2">Bemerkung</p>
                <p className="bg-gray-50 p-4 rounded-lg">{reservation.comment}</p>
              </div>

              <div>
                <p className="text-sm text-gray-600 mb-2">Teilnehmer</p>
                <div className="bg-gray-50 p-4 rounded-lg space-y-2">
                  {reservation.participants.map((participant, index) => (
                    <div key={index} className="flex items-center">
                      <span className="font-medium">
                        {participant.firstName} {participant.lastName}
                      </span>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            <div className="pt-4">
              <Button
                color="primary"
                className="w-full"
                onClick={() => navigate('/')}
              >
                Zur Startseite
              </Button>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
};

