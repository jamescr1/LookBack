import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import { NavLink } from 'react-router-dom';

function Navbar() {
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Investment Portfolio
        </Typography>

        <Button color="inherit" component={NavLink} to="/">
          Home
        </Button>

        <Button color="inherit" component={NavLink} to="/investments">
          Investment Select
        </Button>

        <Button color="inherit" component={NavLink} to="/portfolio">
          Portfolio
        </Button>
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;
