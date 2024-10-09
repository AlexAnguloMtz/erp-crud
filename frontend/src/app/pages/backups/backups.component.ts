import { Component } from '@angular/core';
import { BackupCreationResponse, BackupsService } from '../../services/backup-service';

@Component({
  selector: 'app-backups',
  standalone: true,
  imports: [],
  providers: [BackupsService],
  templateUrl: './backups.component.html',
  styleUrl: './backups.component.css'
})
export class BackupsComponent {

  constructor(
    private backupService: BackupsService,
  ) { }

  onNewBackup(): void {
    this.backupService.newBackup().subscribe({
      next: (response: BackupCreationResponse) => {
        // Assuming response.backup is a Uint8Array
        const backupData: string = response.backup;

        // Create a Blob from the Uint8Array
        const blob = new Blob([backupData], { type: 'application/octet-stream' });

        // Create a URL for the Blob
        const url = window.URL.createObjectURL(blob);

        // Create an anchor element and set its href to the Blob URL
        const a = document.createElement('a');
        a.href = url;
        a.download = response.createdAt.toISOString() + '.sql'; // Set the desired file name

        // Append the anchor to the document body
        document.body.appendChild(a);

        // Programmatically click the anchor to trigger the download
        a.click();

        // Clean up: remove the anchor from the document and revoke the Blob URL
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (error) => {
        console.log(JSON.stringify(error));
      }
    })
  }
}