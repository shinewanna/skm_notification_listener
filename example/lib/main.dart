import 'dart:developer';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:skm_notification_listener/notification_event.dart';
import 'package:skm_notification_listener/skm_notification_listener.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  StreamSubscription<ServiceNotificationEvent>? _subscription;
  List<ServiceNotificationEvent> events = [];

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    TextButton(
                      onPressed: () async {
                        final res =
                            await SkmNotificationListener.requestPermission();
                        log("Is enabled: $res");
                      },
                      child: const Text("Request Permission"),
                    ),
                    const SizedBox(height: 20.0),
                    TextButton(
                      onPressed: () async {
                        final bool res =
                            await SkmNotificationListener.isPermissionGranted();
                        log("Is enabled: $res");
                      },
                      child: const Text("Check Permission"),
                    ),
                    const SizedBox(height: 20.0),
                    TextButton(
                      onPressed: () {
                        _subscription = SkmNotificationListener
                            .notificationsStream
                            .listen((event) {
                          log("$event");
                          setState(() {
                            events.add(event);
                          });
                        });
                      },
                      child: const Text("Start Stream"),
                    ),
                    const SizedBox(height: 20.0),
                    TextButton(
                      onPressed: () {
                        _subscription?.cancel();
                      },
                      child: const Text("Stop Stream"),
                    ),
                  ],
                ),
              ),
              Expanded(
                child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: events.length,
                  itemBuilder: (_, index) => Padding(
                    padding: const EdgeInsets.only(bottom: 8.0),
                    child: ListTile(
                      onTap: () async {
                        try {
                          await events[index]
                              .sendReply("This is an auto response");
                        } catch (e) {
                          log(e.toString());
                        }
                      },
                      trailing: events[index].hasRemoved!
                          ? const Text(
                              "Removed",
                              style: TextStyle(color: Colors.red),
                            )
                          : const SizedBox.shrink(),
                      leading: events[index].appIcon == null
                          ? const SizedBox.shrink()
                          : Image.memory(
                              events[index].appIcon!,
                              width: 35.0,
                              height: 35.0,
                            ),
                      title: Text(events[index].title ?? "No title"),
                      subtitle: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            events[index].content ?? "no content",
                            style: const TextStyle(fontWeight: FontWeight.bold),
                          ),
                          Text(
                            events[index].packageName ?? "no package name",
                            style: const TextStyle(fontWeight: FontWeight.bold),
                          ),
                          const SizedBox(height: 8.0),
                          events[index].canReply!
                              ? const Text(
                                  "Replied with: This is an auto reply",
                                  style: TextStyle(color: Colors.purple),
                                )
                              : const SizedBox.shrink(),
                          events[index].largeIcon != null
                              ? Image.memory(
                                  events[index].largeIcon!,
                                )
                              : const SizedBox.shrink(),
                        ],
                      ),
                      isThreeLine: true,
                    ),
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
