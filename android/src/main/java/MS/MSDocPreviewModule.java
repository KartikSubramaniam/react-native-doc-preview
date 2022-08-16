import React, { Component } from 'react'
const { Platform, NativeModules } = require('react-native')
const { MSDocPreview } = NativeModules

let supportedFormatsAndroid = []

class DocPreview extends Component {
  static isSupported(url) {
    const regex = /([^.]+)$/g
    let extension = regex.exec(url)[1]
    if (Platform.OS === 'ios') {
      return true
    } else {
      return extension &&
        supportedFormatsAndroid.includes(extension.toLowerCase())
        ? true
        : false
    }
  }

  async readAsStringAsync(url) {
    debugger
    let data = await MSDocPreview.readAsStringAsync(url)
    return data
  }

  _shareDoc(url, fileName) {
    MSDocPreview.shareDoc(
      [
        {
          url: url,
          fileName: fileName
        }
      ],
      (error, url) => {
        if (error) {
          let { Alert } = imports('react-native')
          Alert.alert('Android Error', ' No app to support file type', [
            {
              text: 'Continue',
              onPress: () => {},
              style: 'cancel'
            }
          ])
          console.log(error)
        } else {
          console.log(url)
        }
      }
    )
  }
  
 static _openDoc(url, fileName) {
    MSDocPreview.openDoc(
      [
        {
          url: url,
          fileName: fileName
        }
      ],
      (error, url) => {
        if (error) {
          let { Alert } = imports('react-native')
          Alert.alert('Android Error', ' No app to support file type', [
            {
              text: 'Continue',
              onPress: () => {},
              style: 'cancel'
            }
          ])
          console.log(error)
        } else {
          console.log(url)
        }
      }
    )
  }

  render() {
    let { url, fileName } = this.props

    if (DocPreview.isSupported(url)) {
      if (Platform.OS === 'ios') {
        DocPreview._openDoc(url, fileName)
        return null
      } else return null
    } else {
      this._shareDoc(url, fileName)
      return null
    }
  }
}

export { DocPreview }
