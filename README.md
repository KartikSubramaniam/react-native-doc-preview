# react-native-doc-preview
ReactNative: Read Documents within application in iOS and use 3rd party library in-case of Android
## Getting started

`$ yarn add react-native-doc-preview`
## 💻 Usage
```javascript
import { NativeModules } from 'react-native'
let { MSDocPreview } = NativeModules

 MSDocPreview.shareDoc(
   [
     {
       url: path,
       fileName: item.get('name')
     }
   ],
   (error, url) => {
     if (error) {
       let { Dialog } = imports('uikit')

       Dialog.error({
         title: LocaleManager.getLocale('UIkit', 'android-error'),
         description: LocaleManager.getLocale('UIkit', 'no-app-support'),
         positiveText: Locale.continue,
         onPositive: () => {}
       })
     } else {
     }
   }
)

MSDocPreview.openDoc(
  [
    {
      url: path,
      fileName: item.get('name')
    }
  ],
  () => {}
)

or 

import {DocPreview} from 'react-native-doc-preview'

<DocPrewview url={} fileName={}/>
```
## 💡 Api

- **iOS**

 | API                | Params               | Description     |
 | ------------------ | -------------------- | --------------- |
 | `openDoc`          | `url, fileName`      | This api internally uses QLPreview to display the document within the app |
 
- **Android**

 | API                | Params               | Description     |
 | ------------------ | -------------------- | --------------- |
 | `shareDoc`          | `url, fileName`      | This api invokes an Intent to select an app from list of 3rd party apps to support Document Reading |


## 💡 Props

- **General (Android/iOS)**

| Prop                   | Type                | Default | Note                                             |
| ---------------------- | ------------------- | ------- | ------------------------------------------------ |
| `url: mandatory`     | `string`            |         | Path of the file to Read             |
| `fileName: mandatory`                | `string`            |         | Name of the file Read.
           



  
