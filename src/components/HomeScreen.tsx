import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Input, Card, CardBody } from '@nextui-org/react';

export const HomeScreen: React.FC = () => {
  const navigate = useNavigate();
  const [code, setCode] = useState('');
  const [error, setError] = useState('');

  const handleCodeSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!code || code.trim().length === 0) {
      setError('Bitte geben Sie einen Code ein');
      return;
    }

    // Versuche zuerst mit Private Code
    try {
      const response = await fetch(`http://localhost:8081/api/reservations/private/${code.trim()}`);
      if (response.ok) {
        navigate(`/edit/${code.trim()}`);
        return;
      }
    } catch (err) {
      // Ignorieren, versuche Public Code
    }

    // Versuche mit Public Code
    try {
      const response = await fetch(`http://localhost:8081/api/reservations/public/${code.trim()}`);
      if (response.ok) {
        navigate(`/view/${code.trim()}`);
        return;
      }
    } catch (err) {
      // Ignorieren
    }

    setError('Ungültiger Code. Bitte überprüfen Sie Ihre Eingabe.');
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="w-full max-w-2xl space-y-8">
        {/* Header */}
        <div className="text-center">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">Terminkalender</h1>
          <p className="text-gray-600">Raumreservierungssystem</p>
        </div>

        {/* Main Card */}
        <Card className="shadow-xl">
          <CardBody className="p-8 space-y-6">
            {/* Create Reservation Button */}
            <div>
              <Button
                color="primary"
                size="lg"
                className="w-full font-semibold"
                onClick={() => navigate('/create')}
              >
                Reservation erfassen
              </Button>
            </div>

            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300"></div>
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-white text-gray-500">oder</span>
              </div>
            </div>

            {/* Code Input */}
            <form onSubmit={handleCodeSubmit} className="space-y-4">
              <div>
                <Input
                  label="Reservationscode eingeben"
                  placeholder="Private oder Public Code"
                  value={code}
                  onChange={(e) => setCode(e.target.value.toUpperCase())}
                  variant="bordered"
                  size="lg"
                  classNames={{
                    input: "text-center text-lg font-mono",
                    inputWrapper: "border-2"
                  }}
                />
                {error && (
                  <p className="text-red-500 text-sm mt-2">{error}</p>
                )}
              </div>
              <Button
                type="submit"
                color="secondary"
                variant="bordered"
                size="lg"
                className="w-full"
              >
                Code verwenden
              </Button>
            </form>

            {/* View All Reservations */}
            <div className="pt-4">
              <Button
                variant="light"
                size="md"
                className="w-full"
                onClick={() => navigate('/list')}
              >
                Alle Reservationen anzeigen
              </Button>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
};

