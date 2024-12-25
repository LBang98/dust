import './App.css';
import Push from "./component/Push/Push";
import StationChart from "./component/Chart/StationChart";
import Chart from "./component/Chart/Chart";

function App() {
  return (
      <div className="App">
          <div className="Push-container">
              <Push/>
          </div>
          <header className="App-header">
              <Chart/>
              <StationChart/>
          </header>
      </div>
  );
}

export default App;
