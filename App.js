/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Platform,
  TouchableOpacity,
} from 'react-native';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

// We are importing the native Java module here
import {NativeModules} from 'react-native';
var HelloWorld = NativeModules.HelloWorld;

type Props = {};
const App: () => React$Node = () => {

  // eslint-disable-next-line no-undef
  function doPrintInvoice() {
    // Update the document title using the browser API
    HelloWorld.printInvoice(
        JSON.stringify({
          id: '5e79d08c3daf94e4d4',
          name: "Wakaf Al-Quran dan Pembinaan",
          value: [{"detail_id":"5e79d08c5cc56d0ca3","program_code":"WAP","program_name":"Wakaf Al-Quran dan Pembinaan","product_name":"Berinvestasi Pahala di Lereng Gunung Slamet dan Pesisir Tegal","product_code":"0219","product_id":"5c965accd091264051","total":"100.00","quantity":"1.00","unit":"5c94b5b25822647b5b"},{"detail_id":"5e79d08c66020acef9","program_code":"WAFP","program_name":"Water Action For People","product_name":"Krisis Air Bersih; Jangan Biarkan Warga Sukorame Bergantung Pada Air Sumur Rembesan Sungai","product_code":"0246","product_id":"5c965f84c3cbc64d0c","total":"100.00","quantity":"1.00","unit":"5c94b5b25822647b5b"}]
        }),
      err => {
        console.log(err);
      },
      msg => {
        console.log(msg);
      },
    );
  }

  function doPrintCash() {
    // Update the document title using the browser API
    HelloWorld.printCash(
      err => {
        console.log(err);
      },
      msg => {
        console.log(msg);
      },
    );
  }

  function doPrintJson() {
    // Update the document title using the browser API
    let id, values
    fetch("http://202.83.120.140/api/sales-order/detail/5e79d08c3daf94e4d4")
        .then((response) => response.json())
        .then((responseJson) => {
          ()=> {
            id : responseJson.header_id
            values:  responseJson.detail
          }
          console.log('res: '+responseJson.header_id)
          console.log('res: '+JSON.stringify(responseJson))
        })
    console.log('id: '+id)
    console.log('values: '+values)

    HelloWorld.printJson(JSON.stringify({
          id: '5e79d08c3daf94e4d4',
          name: "Wakaf Al-Quran dan Pembinaan",
          value: [{"detail_id":"5e79d08c5cc56d0ca3","program_code":"WAP","program_name":"Wakaf Al-Quran dan Pembinaan","product_name":"Berinvestasi Pahala di Lereng Gunung Slamet dan Pesisir Tegal","product_code":"0219","product_id":"5c965accd091264051","total":"100.00","quantity":"1.00","unit":"5c94b5b25822647b5b"},{"detail_id":"5e79d08c66020acef9","program_code":"WAFP","program_name":"Water Action For People","product_name":"Krisis Air Bersih; Jangan Biarkan Warga Sukorame Bergantung Pada Air Sumur Rembesan Sungai","product_code":"0246","product_id":"5c965f84c3cbc64d0c","total":"100.00","quantity":"1.00","unit":"5c94b5b25822647b5b"}]
        }),
        err => {
          console.log(err);
        },
        msg => {
          console.log(msg);
        },
    );
  }

  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />
          {global.HermesInternal == null ? null : (
            <View style={styles.engine}>
              <Text style={styles.footer}>Engine: Hermes</Text>
            </View>
          )}
          <View style={styles.body}>
            <View style={styles.container}>
              <TouchableOpacity onPress={doPrintInvoice}>
                <Text>Invoke invoice print</Text>
              </TouchableOpacity>
            </View>

            <View style={styles.container}>
              <TouchableOpacity onPress={doPrintCash}>
                <Text>Invokeprint cash</Text>
              </TouchableOpacity>
            </View>

            <View style={styles.container}>
              <TouchableOpacity onPress={doPrintJson}>
                <Text>Invoke JSON print</Text>
              </TouchableOpacity>
            </View>

            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Step One</Text>
              <Text style={styles.sectionDescription}>
                Edit <Text style={styles.highlight}>App.js</Text> to change this
                screen and then come back to see your edits.
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>See Your Changes</Text>
              <Text style={styles.sectionDescription}>
                <ReloadInstructions />
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Debug</Text>
              <Text style={styles.sectionDescription}>
                <DebugInstructions />
              </Text>
            </View>
            <View style={styles.sectionContainer}>
              <Text style={styles.sectionTitle}>Learn More</Text>
              <Text style={styles.sectionDescription}>
                Read the docs to discover what to do next:
              </Text>
            </View>
            <LearnMoreLinks />
          </View>
        </ScrollView>
      </SafeAreaView>
    </>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

export default App;
