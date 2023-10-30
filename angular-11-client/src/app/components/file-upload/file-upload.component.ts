// import { Component, OnInit } from '@angular/core';
// import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
// import { Observable } from 'rxjs';
// import { FileUploadService } from 'src/app/services/file-upload.service';

// @Component({
//   selector: 'app-file-upload',
//   templateUrl: './file-upload.component.html',
//   styleUrls: ['./file-upload.component.css']
// })
// export class FileUploadComponent implements OnInit {
//   selectedFiles?: FileList;
//   currentFile?: File;
//   progress = 0;
//   message = '';

//   fileInfos?: Observable<any>;
//   arr = [];

//   constructor(private uploadService: FileUploadService,private http: HttpClient) { }

//   ngOnInit(): void {
//     this.http.get(`http://localhost:8080/fileApi/files`).subscribe(
//     res => {
//       console.log(typeof(res));
//     }
//     );
//     this.fileInfos = this.uploadService.getFiles();
//   }

//   selectFile(event: any): void {
//     this.selectedFiles = event.target.files;
//   }

//   upload(): void {
//     this.progress = 0;

//     if (this.selectedFiles) {
//       const file: File | null = this.selectedFiles.item(0);

//       if (file) {
//         this.currentFile = file;

//         this.uploadService.upload(this.currentFile).subscribe({
//           next: (event: any) => {
//             if (event.type === HttpEventType.UploadProgress) {
//               this.progress = Math.round(100 * event.loaded / event.total);
//             } else if (event instanceof HttpResponse) {
//               this.message = event.body.message;
//               this.fileInfos = this.uploadService.getFiles();
//             }
//           },
//           error: (err: any) => {
//             console.log(err);
//             this.progress = 0;

//             if (err.error && err.error.message) {
//               this.message = err.error.message;
//             } else {
//               this.message = 'Could not upload the file!';
//             }

//             this.currentFile = undefined;
//           }
//         });
//       }

//       this.selectedFiles = undefined;
//     }
//   }

// }


import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpEvent, HttpEventType, HttpHeaders, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FileUploadService } from 'src/app/services/file-upload.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {
  selectedFiles?: FileList;
  filesToUpload: File[] = []; // Array to hold files for upload
  progress = 0;
  message = '';
  @ViewChild('fileInput') fileInput!: ElementRef;

  fileInfos?: Observable<any>;
  filesUploaded: String[] = [];

  constructor(private uploadService: FileUploadService, private http: HttpClient) { }

  ngOnInit(): void {
    this.uploadService.getFiles().subscribe(
      response => {
        if (Array.isArray(response)) { 
          response.forEach(item => {
            if (item && item.id) { 
              this.filesUploaded.push(item.id);
            }
          });
        }
      }
    );
  }

  addFiles():void{
    if (this.selectedFiles) {
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.filesToUpload.push(this.selectedFiles[i]);
      }
    }
    if (this.fileInput && this.fileInput.nativeElement) {
      this.fileInput.nativeElement.value = '';
    }
  }


  selectFile(event: any): void {
    this.selectedFiles = event.target.files;
  }

  downloadFile(fileId: String): void {
    this.http.get(`http://localhost:8080/fileApi/download/${fileId}`, {
      responseType: 'blob',
      headers: new HttpHeaders().append('Content-Type', 'application/octet-stream')
    }).subscribe(blob => {
      this.downloadBlob(blob, fileId);
    }, error => {
      console.error('Error downloading file:', error);
    });
  }

  private downloadBlob(blob: Blob, filename: String): void {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename + ".zip";
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    a.remove();
  }

  upload(): void {
    this.progress = 0;
    this.message = '';
  
    if (this.filesToUpload && this.filesToUpload.length > 0) {
      this.uploadService.upload(this.filesToUpload).subscribe({
        next: (event: any) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.progress = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.filesUploaded.push(event.body);
            this.filesToUpload.forEach(file => {
              this.message += `File ${file.name} upload successful.\n`;
            });
          }
        },
        error: (err: any) => {
          this.progress = 0;
          this.message += `Files uploading failed: ${err.message}\n`;
          console.error('Upload Error:', err);
        },
        complete: () => {
          console.log("Upload complete");
          this.filesToUpload = [];
        }
      });
    } else {
      this.message = 'No files selected for upload.';
    }
  }
  
}
