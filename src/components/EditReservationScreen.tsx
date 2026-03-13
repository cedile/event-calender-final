import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Input, Textarea, Card, CardBody, Select, SelectItem } from '@nextui-org/react';
import { Reservation, Participant } from '../domain/Reservation';

export const EditReservationScreen: React.FC = () => {
  const navigate = useNavigate();
  const { code } = useParams<{ code: string }>();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  
  const [formData, setFormData] = useState<Reservation>({
    reservationDate: '',
    timeFrom: '',
    timeTo: '',
    roomNumber: 101,
    comment: '',
    participants: []
  });

  const rooms = [101, 102, 103, 104, 105];

  useEffect(() => {
    if (code) {
      fetchReservation();
    }
  }, [code]);

  const fetchReservation = async () => {
    try {
      const response = await fetch(`http://localhost:8081/api/reservations/private/${code}`);
      if (!response.ok) {
        setError('Reservation nicht gefunden');
        setLoading(false);
        return;
      }
      const reservation = await response.json();
      
      // Format date for input
      const date = new Date(reservation.reservationDate);
      const formattedDate = date.toISOString().split('T')[0];
      
      setFormData({
        ...reservation,
        reservationDate: formattedDate,
        timeFrom: reservation.timeFrom.substring(0, 5), // HH:MM
        timeTo: reservation.timeTo.substring(0, 5), // HH:MM
      });
      setLoading(false);
    } catch (err) {
      setError('Fehler beim Laden der Reservation');
      setLoading(false);
    }
  };

  const addParticipant = () => {
    setFormData({
      ...formData,
      participants: [...formData.participants, { firstName: '', lastName: '' }]
    });
  };

  const removeParticipant = (index: number) => {
    if (formData.participants.length > 1) {
      const newParticipants = formData.participants.filter((_, i) => i !== index);
      setFormData({ ...formData, participants: newParticipants });
    }
  };

  const updateParticipant = (index: number, field: 'firstName' | 'lastName', value: string) => {
    const newParticipants = [...formData.participants];
    newParticipants[index] = { ...newParticipants[index], [field]: value };
    setFormData({ ...formData, participants: newParticipants });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSaving(true);

    // Validierung
    if (!formData.reservationDate || !formData.timeFrom || !formData.timeTo) {
      setError('Bitte füllen Sie alle Felder aus');
      setSaving(false);
      return;
    }

    if (formData.comment.length < 10 || formData.comment.length > 200) {
      setError('Bemerkung muss zwischen 10 und 200 Zeichen lang sein');
      setSaving(false);
      return;
    }

    const hasEmptyParticipant = formData.participants.some(
      p => !p.firstName.trim() || !p.lastName.trim()
    );
    if (hasEmptyParticipant) {
      setError('Bitte füllen Sie alle Teilnehmerfelder aus');
      setSaving(false);
      return;
    }

    // Datum validieren
    const selectedDate = new Date(formData.reservationDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    if (selectedDate <= today) {
      setError('Das Datum muss in der Zukunft liegen');
      setSaving(false);
      return;
    }

    // Zeit validieren
    if (formData.timeFrom >= formData.timeTo) {
      setError('Die Zeit \'Von\' muss vor der Zeit \'Bis\' liegen');
      setSaving(false);
      return;
    }

    try {
      const response = await fetch(`http://localhost:8081/api/reservations/private/${code}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const errorData = await response.text();
        setError(errorData || 'Fehler beim Aktualisieren der Reservation');
        setSaving(false);
        return;
      }

      navigate('/list');
    } catch (err) {
      setError('Ein Fehler ist aufgetreten. Bitte versuchen Sie es erneut.');
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!confirm('Möchten Sie diese Reservation wirklich löschen?')) {
      return;
    }

    try {
      const response = await fetch(`http://localhost:8081/api/reservations/private/${code}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        navigate('/list');
      } else {
        setError('Fehler beim Löschen der Reservation');
      }
    } catch (err) {
      setError('Ein Fehler ist aufgetreten.');
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

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
      <div className="max-w-2xl mx-auto">
        <Card className="shadow-xl">
          <CardBody className="p-6 space-y-6">
            <div className="flex items-center justify-between">
              <h1 className="text-2xl font-bold text-gray-800">Reservation bearbeiten</h1>
              <Button
                variant="light"
                size="sm"
                onClick={() => navigate('/')}
              >
                Zurück
              </Button>
            </div>

            {error && (
              <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit} className="space-y-4">
              {/* Datum */}
              <Input
                label="Datum"
                type="date"
                value={formData.reservationDate}
                onChange={(e) => setFormData({ ...formData, reservationDate: e.target.value })}
                required
                variant="bordered"
                min={new Date().toISOString().split('T')[0]}
              />

              {/* Zeit Von */}
              <Input
                label="Von"
                type="time"
                value={formData.timeFrom}
                onChange={(e) => setFormData({ ...formData, timeFrom: e.target.value })}
                required
                variant="bordered"
              />

              {/* Zeit Bis */}
              <Input
                label="Bis"
                type="time"
                value={formData.timeTo}
                onChange={(e) => setFormData({ ...formData, timeTo: e.target.value })}
                required
                variant="bordered"
              />

              {/* Zimmer */}
              <Select
                label="Zimmer"
                selectedKeys={[formData.roomNumber.toString()]}
                onSelectionChange={(keys) => {
                  const selected = Array.from(keys)[0] as string;
                  setFormData({ ...formData, roomNumber: parseInt(selected) });
                }}
                variant="bordered"
                required
              >
                {rooms.map((room) => (
                  <SelectItem key={room.toString()} value={room.toString()}>
                    Zimmer {room}
                  </SelectItem>
                ))}
              </Select>

              {/* Bemerkung */}
              <Textarea
                label="Bemerkung"
                placeholder="10-200 Zeichen"
                value={formData.comment}
                onChange={(e) => setFormData({ ...formData, comment: e.target.value })}
                required
                variant="bordered"
                minRows={3}
                maxLength={200}
              />
              <p className="text-sm text-gray-500">
                {formData.comment.length}/200 Zeichen
              </p>

              {/* Teilnehmer */}
              <div className="space-y-4">
                <div className="flex items-center justify-between">
                  <label className="text-sm font-medium text-gray-700">Teilnehmer</label>
                  <Button
                    type="button"
                    size="sm"
                    variant="flat"
                    onClick={addParticipant}
                  >
                    + Teilnehmer hinzufügen
                  </Button>
                </div>

                {formData.participants.map((participant, index) => (
                  <div key={index} className="flex gap-2 items-end">
                    <Input
                      placeholder="Vorname"
                      value={participant.firstName}
                      onChange={(e) => updateParticipant(index, 'firstName', e.target.value)}
                      required
                      variant="bordered"
                      pattern="[A-Za-z]+"
                      title="Nur Buchstaben erlaubt"
                    />
                    <Input
                      placeholder="Nachname"
                      value={participant.lastName}
                      onChange={(e) => updateParticipant(index, 'lastName', e.target.value)}
                      required
                      variant="bordered"
                      pattern="[A-Za-z]+"
                      title="Nur Buchstaben erlaubt"
                    />
                    {formData.participants.length > 1 && (
                      <Button
                        type="button"
                        color="danger"
                        variant="flat"
                        size="sm"
                        onClick={() => removeParticipant(index)}
                      >
                        Entfernen
                      </Button>
                    )}
                  </div>
                ))}
              </div>

              <div className="flex gap-4 pt-4">
                <Button
                  type="button"
                  color="danger"
                  variant="flat"
                  className="flex-1"
                  onClick={handleDelete}
                >
                  Löschen
                </Button>
                <Button
                  type="button"
                  variant="bordered"
                  className="flex-1"
                  onClick={() => navigate('/')}
                >
                  Abbrechen
                </Button>
                <Button
                  type="submit"
                  color="primary"
                  className="flex-1"
                  isLoading={saving}
                >
                  Speichern
                </Button>
              </div>
            </form>
          </CardBody>
        </Card>
      </div>
    </div>
  );
};

