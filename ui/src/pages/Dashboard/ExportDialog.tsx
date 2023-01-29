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
import TextSnippetIcon from '@mui/icons-material/TextSnippet';
import TableRowsIcon from '@mui/icons-material/TableRows';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import DataObjectIcon from '@mui/icons-material/DataObject';
import InputIcon from '@mui/icons-material/Input';
import { orange } from '@mui/material/colors';

const fileFormats = ['CSV', 'JSON', 'Text', 'PDF'];

export interface SimpleDialogProps {
  open: boolean;
  selectedValue: string;
  onClose: (value: string) => void;
}

function SimpleDialog(props: SimpleDialogProps) {
  const { onClose, selectedValue, open } = props;

  const handleClose = () => {
    onClose(selectedValue);
  };

  const handleListItemClick = (value: string) => {
    onClose(value);
  };

  return (
      <Dialog onClose={handleClose} open={open}>
        <DialogTitle>Select Export Format</DialogTitle>
        <List sx={{ pt: 0 }}>
          {fileFormats.map((format) => (
              <ListItem disableGutters>
                <ListItemButton onClick={() => handleListItemClick(format)} key={format}>
                  <ListItemAvatar>
                    <Avatar sx={{ bgcolor: orange[50], color: orange[600] }}>
                      {format.toLowerCase() === 'csv' &&
                          <TableRowsIcon />
                      }
                      {format.toLowerCase() === 'json' &&
                          <DataObjectIcon />
                      }
                      {format.toLowerCase() === 'text' &&
                          <TextSnippetIcon />
                      }
                      {format.toLowerCase() === 'pdf' &&
                          <PictureAsPdfIcon />
                      }

                    </Avatar>
                  </ListItemAvatar>
                  <ListItemText primary={format} />
                </ListItemButton>
              </ListItem>
          ))}
        </List>
      </Dialog>
  );
}

const ExportDialog = () => {
  const [open, setOpen] = React.useState(false);
  const [selectedValue, setSelectedValue] = React.useState(fileFormats[1]);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = (value: string) => {
    setOpen(false);
    setSelectedValue(value);
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
            selectedValue={selectedValue}
            open={open}
            onClose={handleClose}
        />
      </div>
  );
}
export default ExportDialog
