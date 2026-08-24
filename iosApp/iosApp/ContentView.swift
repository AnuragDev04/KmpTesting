import SwiftUI
import UIKit
import SharedLogic

// MARK: - Domain

struct CareService: Identifiable, Hashable {
    let id: String
    let name: String
    let subtitle: String
    let price: String
    let duration: String
    let icon: String
    let tint: Color
    let description: String
}

struct CareAppointment: Identifiable, Codable {
    let id: String
    let service: String
    let date: Date
    let address: String
    var status: String
    let nurse: String
}

struct CareNurse: Identifiable, Hashable {
    let id: String
    let name: String
    let qualification: String
    let experience: String
    let rating: String
    let phone: String
    let specialization: String
    let bio: String
    let tint: Color
}

struct CareNotification: Identifiable {
    let id: String
    let title: String
    let message: String
    let timestamp: String
    let icon: String
    let tint: Color
    let destination: NotificationDestination
    var isRead: Bool
}

enum NotificationDestination {
    case appointment
    case nurse
    case service
}

@MainActor
final class CareHomeStore: ObservableObject {
    @Published var isLoggedIn: Bool
    @Published var userName: String
    @Published var userPhone: String
    @Published var appointments: [CareAppointment]
    @Published var selectedService: CareService?
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var notifications: [CareNotification]

    let services: [CareService] = [
        CareService(id: "nursing", name: "Home Nursing Visit", subtitle: "Vitals, injections & wound care", price: "₹699", duration: "45–60 mins", icon: "cross.case.fill", tint: .teal, description: "A certified nurse visits your home for professional nursing support, vital checks, injections, and dressing changes."),
        CareService(id: "icu", name: "24/7 ICU Home Nurse", subtitle: "Critical care & continuous monitoring", price: "₹2,499", duration: "24 hour shift", icon: "heart.text.square.fill", tint: .indigo, description: "Dedicated ICU-trained nursing support for ventilator, tracheostomy, feeding tube, and post-ICU recovery care."),
        CareService(id: "elder", name: "Compassionate Elder Care", subtitle: "Daily routine & companionship", price: "₹1,199", duration: "12 hour shift", icon: "figure.2.and.child.holdinghands", tint: .orange, description: "Respectful assistance with mobility, hygiene, medication reminders, meals, and meaningful companionship."),
        CareService(id: "physio", name: "Home Physiotherapy", subtitle: "Rehabilitation at your doorstep", price: "₹799", duration: "45–60 mins", icon: "figure.walk.motion", tint: .purple, description: "Personalized mobility assessment, therapeutic exercises, posture work, and pain-relief therapy at home."),
        CareService(id: "lab", name: "Home Lab Test", subtitle: "Safe sample collection", price: "₹399", duration: "20–30 mins", icon: "testtube.2", tint: .pink, description: "Certified phlebotomists collect samples at home with digital reports delivered quickly."),
        CareService(id: "vaccine", name: "Home Vaccination", subtitle: "Safe, sterile & convenient", price: "₹399", duration: "30–45 mins", icon: "syringe.fill", tint: .green, description: "Cold-chain vaccine delivery, professional administration, and post-vaccine observation in the comfort of home.")
    ]

    let nurses: [CareNurse] = [
        CareNurse(id: "priya", name: "Priya Menon", qualification: "B.Sc Nursing · ICU Certified", experience: "8 years experience", rating: "4.9", phone: "+919876543210", specialization: "Critical care & elder care", bio: "Priya provides calm, attentive home nursing support and keeps families informed throughout every visit.", tint: .purple),
        CareNurse(id: "ankit", name: "Ankit Sharma", qualification: "GNM · Emergency Care", experience: "6 years experience", rating: "4.8", phone: "+919876543211", specialization: "Vitals & post-operative care", bio: "Ankit specializes in recovery visits, wound care, and helping patients regain confidence at home.", tint: .indigo)
    ]

    init() {
        let defaults = UserDefaults.standard
        isLoggedIn = defaults.bool(forKey: "carehome.loggedIn")
        userName = defaults.string(forKey: "carehome.userName") ?? ""
        userPhone = defaults.string(forKey: "carehome.phone") ?? ""
        if let data = defaults.data(forKey: "carehome.appointments"),
           let saved = try? JSONDecoder().decode([CareAppointment].self, from: data) {
            appointments = saved
        } else {
            appointments = []
        }
        notifications = [
            CareNotification(id: "reminder", title: "Appointment reminder", message: "Your Home Nursing visit is tomorrow at 10:00 AM.", timestamp: "10:00 AM", icon: "calendar.badge.clock", tint: .purple, destination: .appointment, isRead: false),
            CareNotification(id: "nurse", title: "Nurse assigned", message: "Priya Menon has been assigned to your upcoming visit.", timestamp: "Yesterday", icon: "person.fill", tint: .teal, destination: .nurse, isRead: false),
            CareNotification(id: "offer", title: "CareHome special offer", message: "Get 20% off your next home lab test.", timestamp: "18 May", icon: "tag.fill", tint: .orange, destination: .service, isRead: false)
        ]
    }

    func login(name: String, phone: String) {
        userName = name.isEmpty ? "Riya Sharma" : name
        userPhone = phone
        isLoggedIn = true
        UserDefaults.standard.set(true, forKey: "carehome.loggedIn")
        UserDefaults.standard.set(userName, forKey: "carehome.userName")
        UserDefaults.standard.set(phone, forKey: "carehome.phone")
    }

    func logout() {
        isLoggedIn = false
        UserDefaults.standard.set(false, forKey: "carehome.loggedIn")
    }

    func markAllNotificationsRead() {
        notifications = notifications.map { item in
            var updated = item
            updated.isRead = true
            return updated
        }
    }

    func book(service: CareService, date: Date, address: String) {
        let appointment = CareAppointment(
            id: "CH\(Int(Date().timeIntervalSince1970))",
            service: service.name,
            date: date,
            address: address,
            status: "Nurse assigned",
            nurse: "Priya Menon"
        )
        appointments.insert(appointment, at: 0)
        if let data = try? JSONEncoder().encode(appointments) {
            UserDefaults.standard.set(data, forKey: "carehome.appointments")
        }
    }
}

// MARK: - App shell

struct ContentView: View {
    @StateObject private var store = CareHomeStore()

    var body: some View {
        Group {
            if store.isLoggedIn {
                MainTabView(store: store)
            } else {
                LoginView(store: store)
            }
        }
        .tint(.teal)
    }
}

struct MainTabView: View {
    @ObservedObject var store: CareHomeStore
    @State private var selectedTab = 0

    var body: some View {
        TabView(selection: $selectedTab) {
            NavigationStack { HomeView(store: store, selectedTab: $selectedTab) }
                .tabItem { Label("Home", systemImage: "house.fill") }.tag(0)
            NavigationStack { ServicesView(store: store) }
                .tabItem { Label("Services", systemImage: "square.grid.2x2.fill") }.tag(1)
            NavigationStack { AppointmentsView(store: store) }
                .tabItem { Label("Bookings", systemImage: "calendar") }.tag(2)
            NavigationStack { ProfileView(store: store) }
                .tabItem { Label("Profile", systemImage: "person.crop.circle") }.tag(3)
        }
    }
}

// MARK: - Authentication

struct LoginView: View {
    @ObservedObject var store: CareHomeStore
    @State private var name = ""
    @State private var phone = ""
    @State private var showValidation = false

    var body: some View {
        ZStack {
            LinearGradient(colors: [.teal.opacity(0.95), .cyan.opacity(0.72)], startPoint: .topLeading, endPoint: .bottomTrailing)
                .ignoresSafeArea()
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    Spacer(minLength: 42)
                    Image(systemName: "cross.case.fill")
                        .font(.system(size: 42, weight: .bold))
                        .foregroundStyle(.white)
                    Text("CareHome")
                        .font(.system(size: 40, weight: .bold, design: .rounded))
                        .foregroundStyle(.white)
                    Text("Trusted care, delivered to your doorstep.")
                        .font(.title3)
                        .foregroundStyle(.white.opacity(0.86))
                    Spacer(minLength: 20)
                    VStack(alignment: .leading, spacing: 16) {
                        Text("Welcome back")
                            .font(.title2.bold())
                        Text("Sign in to book trusted healthcare at home.")
                            .foregroundStyle(.secondary)
                        TextField("Your full name", text: $name)
                            .textContentType(.name)
                            .textFieldStyle(.roundedBorder)
                        TextField("Mobile number", text: $phone)
                            .keyboardType(.phonePad)
                            .textContentType(.telephoneNumber)
                            .textFieldStyle(.roundedBorder)
                        if showValidation { Text("Enter your name and mobile number to continue.").font(.caption).foregroundStyle(.red) }
                        Button {
                            if name.isEmpty || phone.count < 10 { showValidation = true } else { store.login(name: name, phone: phone) }
                        } label: {
                            Text("Continue securely").frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.borderedProminent)
                        .tint(.teal)
                    }
                    .padding(22)
                    .background(.regularMaterial, in: RoundedRectangle(cornerRadius: 24))
                    Text("By continuing, you agree to our Terms and Privacy Policy.")
                        .font(.caption)
                        .foregroundStyle(.white.opacity(0.8))
                        .frame(maxWidth: .infinity, alignment: .center)
                }
                .padding(22)
            }
        }
    }
}

// MARK: - Home and catalog

struct HomeView: View {
    @ObservedObject var store: CareHomeStore
    @Binding var selectedTab: Int
    @State private var showAdvisor = false

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 22) {
                HStack {
                    VStack(alignment: .leading, spacing: 4) {
                        Text("Good morning,").foregroundStyle(.secondary)
                        Text(store.userName.isEmpty ? "there" : store.userName).font(.title.bold())
                    }
                    Spacer()
                    NavigationLink(destination: NotificationsView(store: store)) {
                        Image(systemName: "bell.badge.fill").font(.title3).foregroundStyle(.teal)
                            .padding(12).background(Color.teal.opacity(0.12), in: Circle())
                    }
                }
                HStack(spacing: 14) {
                    Image(systemName: "sparkles").font(.title2).foregroundStyle(.white)
                    VStack(alignment: .leading, spacing: 4) {
                        Text("Not sure what care you need?").font(.headline).foregroundStyle(.white)
                        Text("Ask our AI care advisor").font(.subheadline).foregroundStyle(.white.opacity(0.85))
                    }
                    Spacer()
                    Image(systemName: "chevron.right").foregroundStyle(.white)
                }
                .padding(18)
                .background(LinearGradient(colors: [.teal, .blue], startPoint: .leading, endPoint: .trailing), in: RoundedRectangle(cornerRadius: 20))
                .onTapGesture { showAdvisor = true }
                Text("How can we help today?").font(.title2.bold())
                LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 12) {
                    QuickAction(title: "Book a nurse", icon: "cross.case.fill", color: .teal) { selectedTab = 1 }
                    QuickAction(title: "My bookings", icon: "calendar", color: .indigo) { selectedTab = 2 }
                    QuickAction(title: "Emergency help", icon: "phone.fill", color: .red) { callSupport() }
                    QuickAction(title: "Support", icon: "bubble.left.and.bubble.right.fill", color: .orange) { selectedTab = 3 }
                }
                HStack {
                    Text("Popular services").font(.title2.bold())
                    Spacer()
                    Button("See all") { selectedTab = 1 }.font(.subheadline.weight(.semibold))
                }
                ForEach(Array(store.services.prefix(3))) { service in
                    NavigationLink(destination: ServiceDetailView(service: service, store: store)) {
                        ServiceRow(service: service)
                    }.buttonStyle(.plain)
                }
            }
            .padding()
        }
        .navigationTitle("CareHome")
        .navigationBarTitleDisplayMode(.inline)
        .sheet(isPresented: $showAdvisor) { AdvisorView(store: store) }
    }

    private func callSupport() {
        guard let url = URL(string: "tel://18001234567") else { return }
        UIApplication.shared.open(url)
    }
}

struct QuickAction: View {
    let title: String
    let icon: String
    let color: Color
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            VStack(alignment: .leading, spacing: 12) {
                Image(systemName: icon).font(.title2).foregroundStyle(color)
                Text(title).font(.subheadline.weight(.semibold)).foregroundStyle(.primary)
            }
            .frame(maxWidth: .infinity, alignment: .leading).padding(16)
            .background(color.opacity(0.09), in: RoundedRectangle(cornerRadius: 16))
        }
    }
}

struct ServicesView: View {
    @ObservedObject var store: CareHomeStore
    @State private var search = ""
    var filtered: [CareService] { search.isEmpty ? store.services : store.services.filter { $0.name.localizedCaseInsensitiveContains(search) || $0.subtitle.localizedCaseInsensitiveContains(search) } }
    var body: some View {
        List {
            Section { Text("Professional care, matched to your needs.").font(.subheadline).foregroundStyle(.secondary).listRowBackground(Color.clear) }
            ForEach(filtered) { service in
                NavigationLink(destination: ServiceDetailView(service: service, store: store)) { ServiceRow(service: service).padding(.vertical, 5) }
            }
        }
        .listStyle(.insetGrouped).searchable(text: $search, prompt: "Search services")
        .navigationTitle("All services")
    }
}

struct ServiceRow: View {
    let service: CareService
    var body: some View {
        HStack(spacing: 14) {
            Image(systemName: service.icon).font(.title2).foregroundStyle(service.tint).frame(width: 48, height: 48).background(service.tint.opacity(0.12), in: RoundedRectangle(cornerRadius: 14))
            VStack(alignment: .leading, spacing: 5) {
                Text(service.name).font(.headline)
                Text(service.subtitle).font(.caption).foregroundStyle(.secondary)
                Text("\(service.duration)  •  \(service.price)").font(.caption.weight(.semibold)).foregroundStyle(service.tint)
            }
            Spacer()
        }
    }
}

// MARK: - Booking

struct ServiceDetailView: View {
    let service: CareService
    @ObservedObject var store: CareHomeStore
    @State private var showBooking = false
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 22) {
                Image(systemName: service.icon).font(.system(size: 58)).foregroundStyle(service.tint).frame(maxWidth: .infinity).frame(height: 170).background(service.tint.opacity(0.12), in: RoundedRectangle(cornerRadius: 28))
                Text(service.name).font(.largeTitle.bold())
                HStack { Label(service.duration, systemImage: "clock"); Spacer(); Text(service.price).font(.title2.bold()).foregroundStyle(service.tint) }.foregroundStyle(.secondary)
                Text("About this service").font(.title3.bold())
                Text(service.description).foregroundStyle(.secondary).lineSpacing(5)
                VStack(alignment: .leading, spacing: 12) {
                    Text("What's included").font(.title3.bold())
                    Label("Verified healthcare professional", systemImage: "checkmark.seal.fill")
                    Label("Digital visit summary", systemImage: "doc.text.fill")
                    Label("24/7 CareHome support", systemImage: "headphones")
                }.padding(18).background(Color(.secondarySystemBackground), in: RoundedRectangle(cornerRadius: 18))
                 if let nurse = store.nurses.first {
                         NavigationLink(destination: NurseDetailView(nurse: nurse)) {
                         Label("Meet your care professional", systemImage: "person.crop.circle.badge.checkmark")
                             .frame(maxWidth: .infinity, alignment: .leading)
                     }
                 }
                Button("Book this service") { showBooking = true }.buttonStyle(.borderedProminent).controlSize(.large).frame(maxWidth: .infinity)
            }.padding()
        }
        .navigationTitle("Service details").navigationBarTitleDisplayMode(.inline)
        .sheet(isPresented: $showBooking) { BookingView(service: service, store: store) }
    }
}

struct BookingView: View {
    let service: CareService
    @ObservedObject var store: CareHomeStore
    @Environment(\.dismiss) private var dismiss
    @State private var date = Calendar.current.date(byAdding: .day, value: 1, to: Date()) ?? Date()
    @State private var address = "24 Park Street, Bangalore"
    @State private var showPayment = false
    var body: some View {
        NavigationStack {
            Form {
                Section("Visit details") {
                    DatePicker("Preferred date", selection: $date, in: Date()..., displayedComponents: [.date, .hourAndMinute])
                    TextField("Care address", text: $address)
                }
                Section("Selected service") { ServiceRow(service: service) }
                Section("Payment") {
                    HStack { Text("Service fee"); Spacer(); Text(service.price).bold() }
                    Label("Secure payment after confirmation", systemImage: "lock.shield.fill").font(.caption).foregroundStyle(.secondary)
                }
                Section {
                    Button("Continue to payment") { showPayment = true }
                }
            }
            .navigationTitle("Book a visit").toolbar { ToolbarItem(placement: .cancellationAction) { Button("Cancel") { dismiss() } } }
            .sheet(isPresented: $showPayment) { PaymentView(service: service, date: date, address: address, store: store) }
        }
    }
}

struct PaymentView: View {
    let service: CareService
    let date: Date
    let address: String
    @ObservedObject var store: CareHomeStore
    @Environment(\.dismiss) private var dismiss
    @State private var method = "UPI"
    @State private var booked = false
    var body: some View {
        NavigationStack {
            VStack(spacing: 22) {
                Image(systemName: booked ? "checkmark.circle.fill" : "creditcard.fill").font(.system(size: 58)).foregroundStyle(booked ? .green : .teal)
                Text(booked ? "Booking confirmed" : "Complete payment").font(.title.bold())
                if booked {
                    Text("Your nurse has been assigned. You can track the visit from Bookings.").multilineTextAlignment(.center).foregroundStyle(.secondary)
                    Button("Done") { dismiss() }.buttonStyle(.borderedProminent)
                } else {
                    VStack(alignment: .leading, spacing: 12) {
                        Text(service.name).font(.headline)
                        HStack { Text("Total"); Spacer(); Text(service.price).font(.title2.bold()) }
                        Picker("Payment method", selection: $method) { Text("UPI").tag("UPI"); Text("Card").tag("Card"); Text("Net banking").tag("Net banking") }.pickerStyle(.segmented)
                    }.padding().background(Color(.secondarySystemBackground), in: RoundedRectangle(cornerRadius: 18))
                    Button("Pay securely") { store.book(service: service, date: date, address: address); withAnimation { booked = true } }.buttonStyle(.borderedProminent).controlSize(.large)
                }
                Spacer()
            }.padding().navigationTitle("Checkout").navigationBarTitleDisplayMode(.inline)
        }
    }
}

// MARK: - Appointments and tracking

struct AppointmentsView: View {
    @ObservedObject var store: CareHomeStore
    var body: some View {
        Group {
            if store.appointments.isEmpty {
                ContentUnavailableView("No bookings yet", systemImage: "calendar.badge.plus", description: Text("Your confirmed care visits will appear here."))
            } else {
                List(store.appointments) { appointment in
                    NavigationLink(destination: AppointmentDetailView(appointment: appointment)) { AppointmentRow(appointment: appointment) }
                }.listStyle(.insetGrouped)
            }
        }.navigationTitle("My bookings")
    }
}

struct AppointmentRow: View {
    let appointment: CareAppointment
    var body: some View {
        HStack(spacing: 14) {
            Image(systemName: "calendar.badge.clock").font(.title2).foregroundStyle(.teal).frame(width: 44, height: 44).background(Color.teal.opacity(0.12), in: RoundedRectangle(cornerRadius: 12))
            VStack(alignment: .leading, spacing: 5) {
                Text(appointment.service).font(.headline)
                Text(appointment.date.formatted(date: .abbreviated, time: .shortened)).font(.caption).foregroundStyle(.secondary)
                Text(appointment.status).font(.caption.weight(.semibold)).foregroundStyle(.green)
            }
        }
    }
}

struct AppointmentDetailView: View {
    let appointment: CareAppointment
    @State private var showCancel = false
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                HStack { Image(systemName: "checkmark.seal.fill").foregroundStyle(.green); Text("Nurse assigned").font(.headline); Spacer() }
                    .padding().background(Color.green.opacity(0.1), in: RoundedRectangle(cornerRadius: 16))
                Text(appointment.service).font(.title.bold())
                DetailLine(icon: "calendar", title: "Visit time", value: appointment.date.formatted(date: .complete, time: .shortened))
                DetailLine(icon: "mappin.and.ellipse", title: "Care address", value: appointment.address)
                DetailLine(icon: "person.crop.circle.fill", title: "Assigned nurse", value: appointment.nurse)
                TrackingTimeline()
                 NavigationLink(destination: NurseDetailView(nurse: storeNurse(for: appointment.nurse))) {
                     Label("View nurse profile", systemImage: "person.crop.circle")
                         .frame(maxWidth: .infinity, alignment: .leading)
                 }
                 NavigationLink(destination: NurseTrackingView(appointment: appointment)) {
                     Label("Track nurse arrival", systemImage: "location.fill")
                         .frame(maxWidth: .infinity, alignment: .leading)
                 }
                Button { callNurse() } label: { Label("Call \(appointment.nurse)", systemImage: "phone.fill").frame(maxWidth: .infinity) }.buttonStyle(.borderedProminent)
                Button("Cancel booking", role: .destructive) { showCancel = true }.frame(maxWidth: .infinity)
            }.padding()
        }.navigationTitle("Booking details").navigationBarTitleDisplayMode(.inline).alert("Cancel booking?", isPresented: $showCancel) { Button("Keep booking", role: .cancel) {}; Button("Cancel", role: .destructive) {} } message: { Text("Our support team can help reschedule this visit instead.") }
    }
    private func storeNurse(for name: String) -> CareNurse {
        CareNurse(id: "assigned", name: name, qualification: "Certified Care Professional", experience: "Trusted CareHome professional", rating: "4.9", phone: "+919876543210", specialization: "Home healthcare", bio: "Your assigned CareHome professional is ready to support your visit.", tint: .teal)
    }
    private func callNurse() { if let url = URL(string: "tel://+919876543210") { UIApplication.shared.open(url) } }
}

struct DetailLine: View {
    let icon: String; let title: String; let value: String
    var body: some View { HStack(alignment: .top, spacing: 14) { Image(systemName: icon).foregroundStyle(.teal).frame(width: 22); VStack(alignment: .leading, spacing: 4) { Text(title).font(.caption).foregroundStyle(.secondary); Text(value).font(.body) }; Spacer() } }
}

struct TrackingTimeline: View {
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text("Visit status").font(.title3.bold()).padding(.bottom, 14)
            ForEach(Array(["Booking confirmed", "Nurse assigned", "Nurse on the way", "Care in progress"].enumerated()), id: \.offset) { index, step in
                HStack(alignment: .top, spacing: 14) {
                    VStack(spacing: 0) { Image(systemName: index < 2 ? "checkmark.circle.fill" : "circle").foregroundStyle(index < 2 ? .green : .secondary); if index < 3 { Rectangle().fill(Color.secondary.opacity(0.25)).frame(width: 1, height: 28) } }
                    Text(step).font(index < 2 ? .body.weight(.semibold) : .body).foregroundStyle(index < 2 ? .primary : .secondary).padding(.bottom, 18)
                }
            }
        }.padding(18).background(Color(.secondarySystemBackground), in: RoundedRectangle(cornerRadius: 18))
    }
}

// MARK: - Native iOS nurse and notification screens

struct NurseDetailView: View {
    let nurse: CareNurse
    @State private var showMessage = false

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                VStack(spacing: 12) {
                    Image(systemName: "person.crop.circle.fill")
                        .font(.system(size: 78))
                        .foregroundStyle(nurse.tint)
                    Text(nurse.name).font(.title.bold())
                    Text(nurse.qualification).foregroundStyle(.secondary)
                    HStack {
                        Label(nurse.rating, systemImage: "star.fill").foregroundStyle(.orange)
                        Text("· \(nurse.experience)").foregroundStyle(.secondary)
                    }
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 20)
                .background(nurse.tint.opacity(0.1), in: RoundedRectangle(cornerRadius: 24))

                VStack(alignment: .leading, spacing: 12) {
                    Text("Specialization").font(.headline)
                    Text(nurse.specialization).foregroundStyle(.secondary)
                    Text("About \(nurse.name)").font(.headline)
                    Text(nurse.bio).foregroundStyle(.secondary).lineSpacing(4)
                }
                .padding()
                .background(Color(.secondarySystemBackground), in: RoundedRectangle(cornerRadius: 18))

                HStack(spacing: 12) {
                    Button {
                        if let url = URL(string: "tel://\(nurse.phone)") {
                            UIApplication.shared.open(url)
                        }
                    } label: {
                        Label("Call", systemImage: "phone.fill")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.borderedProminent)

                    Button {
                        showMessage = true
                    } label: {
                        Label("Message", systemImage: "message.fill")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.bordered)
                }
            }
            .padding()
        }
        .navigationTitle("Nurse profile")
        .navigationBarTitleDisplayMode(.inline)
        .alert("Message nurse", isPresented: $showMessage) {
            Button("Done", role: .cancel) {}
        } message: {
            Text("Messaging will be available when CareHome chat is connected.")
        }
    }
}

struct NotificationsView: View {
    @ObservedObject var store: CareHomeStore

    var body: some View {
        List {
            if store.notifications.isEmpty {
                ContentUnavailableView("You're all caught up", systemImage: "bell.slash", description: Text("New appointment and care updates will appear here."))
            } else {
                ForEach(store.notifications) { notification in
                    HStack(alignment: .top, spacing: 14) {
                        Image(systemName: notification.icon)
                            .foregroundStyle(notification.tint)
                            .frame(width: 38, height: 38)
                            .background(notification.tint.opacity(0.12), in: Circle())
                        VStack(alignment: .leading, spacing: 5) {
                            HStack {
                                Text(notification.title).font(.headline)
                                Spacer()
                                Text(notification.timestamp).font(.caption).foregroundStyle(.secondary)
                            }
                            Text(notification.message).font(.subheadline).foregroundStyle(.secondary)
                        }
                    }
                    .padding(.vertical, 6)
                    .listRowBackground(notification.isRead ? Color.clear : Color.teal.opacity(0.06))
                    .onTapGesture {
                        if let index = store.notifications.firstIndex(where: { $0.id == notification.id }) {
                            store.notifications[index].isRead = true
                        }
                    }
                }
            }
        }
        .listStyle(.insetGrouped)
        .navigationTitle("Notifications")
        .toolbar {
            if !store.notifications.isEmpty {
                ToolbarItem(placement: .topBarTrailing) {
                    Button("Mark read") { store.markAllNotificationsRead() }
                }
            }
        }
    }
}

struct NurseTrackingView: View {
    let appointment: CareAppointment

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 18) {
                HStack(spacing: 14) {
                    Image(systemName: "location.fill").font(.title2).foregroundStyle(.teal)
                    VStack(alignment: .leading, spacing: 4) {
                        Text("Your nurse is assigned").font(.headline)
                        Text(appointment.nurse).foregroundStyle(.secondary)
                    }
                }
                .padding()
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color.teal.opacity(0.1), in: RoundedRectangle(cornerRadius: 18))

                VStack(alignment: .leading, spacing: 0) {
                    Text("Visit status").font(.title3.bold()).padding(.bottom, 16)
                    ForEach(Array(["Booking confirmed", "Nurse assigned", "Nurse on the way", "Care in progress"].enumerated()), id: \.offset) { index, step in
                        HStack(alignment: .top, spacing: 14) {
                            VStack(spacing: 0) {
                                Image(systemName: index < 2 ? "checkmark.circle.fill" : "circle")
                                    .foregroundStyle(index < 2 ? .green : .secondary)
                                if index < 3 { Rectangle().fill(Color.secondary.opacity(0.25)).frame(width: 1, height: 32) }
                            }
                            Text(step)
                                .font(index < 2 ? .body.weight(.semibold) : .body)
                                .foregroundStyle(index < 2 ? .primary : .secondary)
                                .padding(.bottom, 18)
                        }
                    }
                }
                .padding()
                .background(Color(.secondarySystemBackground), in: RoundedRectangle(cornerRadius: 18))
            }
            .padding()
        }
        .navigationTitle("Track visit")
        .navigationBarTitleDisplayMode(.inline)
    }
}

// MARK: - Profile, support, advisor

struct ProfileView: View {
    @ObservedObject var store: CareHomeStore
    @State private var showSupport = false
    var body: some View {
        List {
            Section {
                HStack(spacing: 14) { Image(systemName: "person.crop.circle.fill").font(.system(size: 58)).foregroundStyle(.teal); VStack(alignment: .leading) { Text(store.userName).font(.title3.bold()); Text(store.userPhone).foregroundStyle(.secondary) } }
            }
            Section("CareHome") {
                NavigationLink(destination: AppointmentsView(store: store)) { Label("My bookings", systemImage: "calendar") }
                Button { showSupport = true } label: { Label("Help & support", systemImage: "questionmark.circle") }
                NavigationLink(destination: AdvisorView(store: store)) { Label("AI care advisor", systemImage: "sparkles") }
                Label("Privacy & security", systemImage: "lock.shield")
            }
            Section { Button("Sign out", role: .destructive) { store.logout() } }
        }.listStyle(.insetGrouped).navigationTitle("Profile").sheet(isPresented: $showSupport) { SupportView() }
    }
}

struct SupportView: View {
    @Environment(\.dismiss) private var dismiss
    @State private var message = ""
    @State private var submitted = false
    var body: some View {
        NavigationStack {
            Form {
                Section("How can we help?") { TextField("Tell us what happened", text: $message, axis: .vertical).lineLimit(4...8) }
                Section { Button(submitted ? "Ticket submitted" : "Submit support ticket") { submitted = true }.disabled(message.isEmpty) }
                Section { Button { if let url = URL(string: "tel://18001234567") { UIApplication.shared.open(url) } } label: { Label("Call 1800 123 4567", systemImage: "phone.fill") } }
            }.navigationTitle("Support").toolbar { ToolbarItem(placement: .cancellationAction) { Button("Close") { dismiss() } } }
        }
    }
}

struct AdvisorView: View {
    @ObservedObject var store: CareHomeStore
    @State private var question = ""
    @State private var answer: String?
    @State private var isLoading = false
    var body: some View {
        NavigationStack {
            VStack(spacing: 18) {
                Image(systemName: "sparkles").font(.system(size: 42)).foregroundStyle(.teal)
                Text("AI Care Advisor").font(.title.bold())
                Text("Share a few details about the care you’re looking for. This is guidance, not a medical diagnosis.").multilineTextAlignment(.center).foregroundStyle(.secondary)
                if isLoading { ProgressView("Reviewing your needs…").padding() }
                if let answer { Text(answer).frame(maxWidth: .infinity, alignment: .leading).padding(18).background(Color.teal.opacity(0.1), in: RoundedRectangle(cornerRadius: 18)) }
                Spacer()
                HStack {
                    TextField("e.g. care after knee surgery", text: $question, axis: .vertical).textFieldStyle(.roundedBorder)
                    Button { askAdvisor() } label: { Image(systemName: "arrow.up.circle.fill").font(.title) }.disabled(question.isEmpty || isLoading)
                }
            }.padding().navigationTitle("Care advisor").navigationBarTitleDisplayMode(.inline)
        }
    }
    private func askAdvisor() {
        isLoading = true
        let prompt = question.lowercased()
        Task { @MainActor in
            try? await Task.sleep(for: .milliseconds(550))
            if prompt.contains("elder") || prompt.contains("senior") {
                answer = "Compassionate Elder Care may be a good fit. It includes mobility assistance, medication reminders, daily routine support, and companionship."
            } else if prompt.contains("surgery") || prompt.contains("operation") {
                answer = "Home Nursing Visit or Home Physiotherapy can support recovery. A nurse can monitor your wound and vitals; a physiotherapist can help restore safe movement."
            } else if prompt.contains("critical") || prompt.contains("icu") {
                answer = "Please speak with your doctor first. For medically approved home recovery, our 24/7 ICU Home Nurse service offers continuous monitoring and critical-care support."
            } else {
                answer = "A Home Nursing Visit is a helpful starting point for vitals, injections, wound care, and a professional assessment. Our care team can guide you to the right package."
            }
            isLoading = false
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}