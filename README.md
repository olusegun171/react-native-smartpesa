
# react-native-smartpesa-sdk

## Getting started

`$ npm install react-native-smartpesa-sdk --save`

### Mostly automatic installation

`$ react-native link react-native-smartpesa-sdk`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-smartpesa-sdk` and add `RNSmartpesaSdk.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNSmartpesaSdk.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.smatecsystems.smartpesa.RNSmartpesaSdkPackage;` to the imports at the top of the file
  - Add `new RNSmartpesaSdkPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-smartpesa-sdk'
  	project(':react-native-smartpesa-sdk').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-smartpesa-sdk/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-smartpesa-sdk')
  	```


## Usage
```javascript
import RNSmartpesaSdk from 'react-native-smartpesa-sdk';

// TODO: What to do with the module?
RNSmartpesaSdk;
```
  