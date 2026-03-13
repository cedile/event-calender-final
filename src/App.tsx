import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { NextUIProvider } from "@nextui-org/react";
import { HomeScreen } from "./components/HomeScreen";
import { CreateReservationScreen } from "./components/CreateReservationScreen";
import { ConfirmationScreen } from "./components/ConfirmationScreen";
import { EditReservationScreen } from "./components/EditReservationScreen";
import { ViewReservationScreen } from "./components/ViewReservationScreen";
import { ReservationsListScreen } from "./components/ReservationsListScreen";

function App() {
  return (
    <NextUIProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeScreen />} />
          <Route path="/create" element={<CreateReservationScreen />} />
          <Route path="/confirmation" element={<ConfirmationScreen />} />
          <Route path="/edit/:code" element={<EditReservationScreen />} />
          <Route path="/view/:code" element={<ViewReservationScreen />} />
          <Route path="/list" element={<ReservationsListScreen />} />
        </Routes>
      </BrowserRouter>
    </NextUIProvider>
  );
}

export default App;