import * as React from 'react';
import Button from '@mui/material/Button';
import Avatar from '@mui/material/Avatar';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import InputIcon from '@mui/icons-material/Input';


interface ExportTarget {
  platform: string,
  logo: any,
  format: string
}

const platforms: ExportTarget[] = [
  {
    platform: 'WordPress',
    logo: "/assets/images/wordpress.svg",
    format: 'html'
  },
  {
    platform: 'Notion',
    logo: "/assets/images/notion.svg",
    format: 'html'
  },
  {
    platform: 'PDF',
    logo: "/assets/images/pdf.svg",
    format: 'pdf'
  },
  {
    platform: 'Text',
    logo: "/assets/images/text.svg",
    format: 'txt'
  }
]

export interface SimpleDialogProps {
  open: boolean;
  onClose: () => void;
}

function SimpleDialog(props: SimpleDialogProps) {
  const { onClose, open } = props;

  const handleClose = () => {
    onClose();
  };

  const handleListItemClick = (value: string) => {
    onClose();
  };

  return (
      <Dialog onClose={handleClose} open={open}>
        <DialogTitle sx={{textAlign: 'center', p: 4}}>Select Export Target</DialogTitle>
        <List sx={{ pt: 0 }}>
          {platforms.map((p) => (
              <ListItem  key={p.platform}>
                <ListItemButton onClick={() => handleListItemClick(p.format)}>
                  <ListItemAvatar>
                    <Avatar sx={{ bgcolor: '#fff', color: '#fff', borderRadius: '5px' }}>
                      <img src={p.logo} alt={p.platform} width={256}/>
                    </Avatar>
                  </ListItemAvatar>
                  <ListItemText primary={p.platform} />
                </ListItemButton>
              </ListItem>
          ))}
        </List>
      </Dialog>
  );
}

const ExportDialog = () => {
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
      <div>
        <Button
            color="info"
            startIcon={<InputIcon/>}
            onClick={handleClickOpen}
            size='large'
            variant='text'>
          Export
        </Button>
        <SimpleDialog
            open={open}
            onClose={handleClose}
        />
      </div>
  );
}
export default ExportDialog
