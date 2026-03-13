import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Button, Card, CardBody, Code } from '@nextui-org/react';

export const ConfirmationScreen: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [privateCode, setPrivateCode] = useState<string>('');
  const [publicCode, setPublicCode] = useState<string>('');

  useEffect(() => {
    if (location.state) {
      setPrivateCode(location.state.privateCode || '');
      setPublicCode(location.state.publicCode || '');
    }
  }, [location]);

  const copyToClipboard = (text: string, type: string) => {
    navigator.clipboard.writeText(text).then(() => {
      alert(`${type} Code wurde in die Zwischenablage kopiert!`);
    });
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 to-emerald-100 flex items-center justify-center p-4">
      <div className="w-full max-w-2xl">
        <Card className="shadow-xl">
          <CardBody className="p-8 space-y-6">
            <div className="text-center">
              <div className="text-6xl mb-4">✅</div>
              <h1 className="text-3xl font-bold text-gray-800 mb-2">
                Reservation erfolgreich erstellt!
              </h1>
              <p className="text-gray-600">
                Bitte notieren Sie sich die folgenden Codes
              </p>
            </div>

            <div className="space-y-4">
              {/* Private Code */}
              <div className="bg-blue-50 p-4 rounded-lg border-2 border-blue-200">
                <div className="flex items-center justify-between mb-2">
                  <h3 className="font-semibold text-blue-800">Private Code</h3>
                  <Button
                    size="sm"
                    variant="flat"
                    onClick={() => copyToClipboard(privateCode, 'Private')}
                  >
                    Kopieren
                  </Button>
                </div>
                <Code className="text-lg font-mono w-full p-3 bg-white">
                  {privateCode}
                </Code>
                <p className="text-sm text-gray-600 mt-2">
                  Mit diesem Code können Sie die Reservation bearbeiten oder löschen.
                </p>
              </div>

              {/* Public Code */}
              <div className="bg-green-50 p-4 rounded-lg border-2 border-green-200">
                <div className="flex items-center justify-between mb-2">
                  <h3 className="font-semibold text-green-800">Public Code</h3>
                  <Button
                    size="sm"
                    variant="flat"
                    onClick={() => copyToClipboard(publicCode, 'Public')}
                  >
                    Kopieren
                  </Button>
                </div>
                <Code className="text-lg font-mono w-full p-3 bg-white">
                  {publicCode}
                </Code>
                <p className="text-sm text-gray-600 mt-2">
                  Mit diesem Code können Teilnehmer die Reservation einsehen.
                </p>
              </div>
            </div>

            <div className="flex gap-4 pt-4">
              <Button
                variant="bordered"
                className="flex-1"
                onClick={() => navigate('/')}
              >
                Zur Startseite
              </Button>
              <Button
                color="primary"
                className="flex-1"
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

